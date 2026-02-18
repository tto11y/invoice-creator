package com.teatown.software.invoice.adapter.out.pdf;

import com.teatown.software.invoice.application.exception.InvoicePdfException;
import com.teatown.software.invoice.application.port.PdfGenerationPort;
import com.teatown.software.invoice.configuration.InvoicePdfProperties;
import com.lowagie.text.*;
import com.lowagie.text.alignment.HorizontalAlignment;
import com.lowagie.text.alignment.VerticalAlignment;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.teatown.software.invoice.domain.CompanyDetails;
import com.teatown.software.invoice.domain.Customer;
import com.teatown.software.invoice.domain.Invoice;
import com.teatown.software.invoice.domain.InvoiceItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * Outbound adapter: generates invoice PDF using OpenPDF.
 */
@Component
public class OpenPdfInvoiceGenerator implements PdfGenerationPort {

    private static final Logger log = LoggerFactory.getLogger(OpenPdfInvoiceGenerator.class);

    private final MessageSource messageSource;
    private final DateTimeFormatter dateFormat;
    private final float margin;
    private final Font titleFont;
    private final Font headingFont;
    private final Font normalFont;
    private final Font smallFont;

    public OpenPdfInvoiceGenerator(final InvoicePdfProperties properties, final MessageSource messageSource) {
        this.messageSource = messageSource;
        this.dateFormat = DateTimeFormatter.ofPattern(properties.getDateFormat());
        this.margin = properties.getMarginMm();
        this.titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, properties.getTitleFontSize());
        this.headingFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, properties.getHeadingFontSize());
        this.normalFont = FontFactory.getFont(FontFactory.HELVETICA, properties.getNormalFontSize());
        this.smallFont = FontFactory.getFont(FontFactory.HELVETICA, properties.getSmallFontSize());
    }

    @Override
    public byte[] generate(final Invoice invoice, final Locale locale) {

        try (final var baos = new ByteArrayOutputStream()) {
            final var document = new Document(PageSize.A4, margin, margin, margin, margin);
            PdfWriter.getInstance(document, baos);

            writeCompanyFooterTable(document, invoice.companyDetails(), locale);

            document.open();

            float y = writeCompanyLogo(document);
            y = writeCompanyHeader(document, invoice.companyDetails(), y, locale);
            y = writeInvoiceDetails(document, invoice, y, locale);
            y = writeItemsTable(document, invoice.items(), y, locale);
            y = writeTotals(document, invoice, y, locale);
            writeFinalNotes(document, invoice.finalNotes(), locale);

            document.close();
            return baos.toByteArray();
        } catch (DocumentException | IOException e) {
            log.error("Failed to generate invoice PDF for invoice {}", invoice.invoiceNumber(), e);
            throw new InvoicePdfException("Failed to generate invoice PDF", e);
        }

    }

    private float writeCompanyLogo(Document document) {
        Jpeg jpeg;
        try (final var in = new FileInputStream("./src/main/resources/2026-02-14_Teatown-Software_T-Logo.jpg")) {
            jpeg = new Jpeg(in.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final var indentRight = 40f;

        // todo add proper rounding
        final var downScaledWidth = 110;
        jpeg.scaleToFit(downScaledWidth, 100.64f);

        final Rectangle pageSize = document.getPageSize();
        float indentLeft = pageSize.getWidth() - document.leftMargin() - jpeg.getScaledWidth() - 50;

        final var p = new Paragraph();
        p.add(jpeg);
        p.setIndentationLeft(indentLeft);
        p.setSpacingAfter(34);

        document.add(p);

        return 0;
    }

    private float writeCompanyHeader(final Document document, final CompanyDetails company, final float fromY, final Locale locale) throws DocumentException {
        final var addr = company.address();

        final var topLine = String.join(" - ",
                company.name(),
                addr.streetAndNumber(),
                addr.postalCode() + " " + addr.city(),
                addr.countryName(locale)
        );
        final var p = new Paragraph(topLine, smallFont);
        p.setSpacingAfter(34);
        document.add(p);

        return 0;
    }

    private String msg(final String code, final Locale locale) {
        return messageSource.getMessage(code, null, locale);
    }

    private String msg(final String code, final Object[] args, final Locale locale) {
        return messageSource.getMessage(code, args, locale);
    }

    private float writeInvoiceDetails(final Document document, final Invoice invoice, final float fromY, final Locale locale) throws DocumentException {
        final var t = new PdfPTable(2);
        t.setWidthPercentage(100);
        t.setSpacingAfter(13);

        t.addCell(customerAddressCellWithoutBorder(invoice.customer(), locale));
        t.addCell(invoiceMetaCell(invoice, locale));

        document.add(t);

        document.add(new Paragraph(" ", normalFont));

        if (Boolean.TRUE.equals(invoice.reverseCharge())) {
            final var p = new Paragraph(msg("invoice.pdf.reverseCharge", locale), headingFont);
            p.setSpacingAfter(21);
            document.add(p);
            document.add(new Paragraph(" ", normalFont));
        }

        return 0;
    }

    private PdfPCell customerAddressCellWithoutBorder(final Customer c, final Locale locale) {
        final var nestedTable1 = new PdfPTable(1);

        final var addr = c.address();

        final var customerDetails = String.join("\n",
                c.companyName(),
                addr.streetAndNumber(),
                addr.postalCode() + " " + addr.city(),
                addr.countryName(locale)
        );

        final PdfPCell cell = cell(customerDetails);
        cell.setVerticalAlignment(VerticalAlignment.CENTER.getId());
        cell.setBorderWidth(0f);

        nestedTable1.addCell(cell);

        final var result = new PdfPCell(nestedTable1);
        result.setBorderWidth(0f);

        return result;
    }

    private PdfPCell invoiceMetaCell(final Invoice inv, final Locale locale) {

        final var invoiceMeta = new PdfPTable(2);

        invoiceMeta.addCell(headerCellWithoutBorder(msg("invoice.pdf.invoiceNo", locale)));
        invoiceMeta.addCell(headerCellRightWithoutBorder(inv.invoiceNumber()));
        invoiceMeta.addCell(cellWithoutBorder(msg("invoice.pdf.invoiceDate", locale)));
        invoiceMeta.addCell(cellRightWithoutBorder(inv.invoiceDate().format(dateFormat)));
        invoiceMeta.addCell(cellWithoutBorder(msg("invoice.pdf.deliveryDate", locale)));
        invoiceMeta.addCell(cellRightWithoutBorder(inv.deliveryDate().format(dateFormat)));
        invoiceMeta.addCell(cellWithoutBorder(msg("invoice.pdf.dueDate", locale)));
        invoiceMeta.addCell(cellRightWithoutBorder(inv.dueDate().format(dateFormat)));
        invoiceMeta.addCell(cellWithoutBorder(" "));
        invoiceMeta.addCell(cellWithoutBorder(" "));

        final var c = inv.customer();

        invoiceMeta.addCell(cellWithoutBorder(msg("invoice.pdf.yourCustomerNo", locale)));
        invoiceMeta.addCell(cellRightWithoutBorder(c.customerNumber()));
        if (c.vatId() != null && !c.vatId().isBlank()) {
            invoiceMeta.addCell(cellWithoutBorder(msg("invoice.pdf.yourVatId", locale)));
            invoiceMeta.addCell(cellRightWithoutBorder(c.vatId()));
        }
        invoiceMeta.addCell(cellWithoutBorder(msg("invoice.pdf.yourContact", locale)));
        invoiceMeta.addCell(cellRightWithoutBorder(c.contact()));
        invoiceMeta.addCell(" ");

        final var result = new PdfPCell(invoiceMeta);
        result.setBorderWidth(0f);

        return result;
    }

    private float writeItemsTable(final Document document, final List<InvoiceItem> items, final float fromY, final Locale locale) throws DocumentException {
        document.add(new Paragraph(msg("invoice.pdf.positionDescription", locale), headingFont));

        final float[] widths = {8, 40, 8, 10, 12, 12};
        final var table = new PdfPTable(widths);

        table.setSpacingBefore(10);

        table.setWidthPercentage(100);
        table.setHeaderRows(1);
        table.addCell(headerCellWithBottomBorder(msg("invoice.pdf.pos", locale)));
        table.addCell(headerCellWithBottomBorder(msg("invoice.pdf.description", locale)));
        table.addCell(headerCellRightWithBottomBorder(msg("invoice.pdf.qty", locale)));
        table.addCell(headerCellRightWithBottomBorder(msg("invoice.pdf.unit", locale)));
        table.addCell(headerCellRightWithBottomBorder(msg("invoice.pdf.unitPrice", locale)));
        table.addCell(headerCellRightWithBottomBorder(msg("invoice.pdf.total", locale)));

        int pos = 1;
        for (final InvoiceItem item : items) {
            table.addCell(cellWithoutBorder(String.valueOf(pos++)));
            table.addCell(cellWithoutBorder(item.description()));
            table.addCell(cellRightWithoutBorder(formatDecimal(item.quantity())));
            table.addCell(cellRightWithoutBorder(item.unit()));
            table.addCell(cellRightWithoutBorder(formatMoney(item.unitPriceEuro())));
            table.addCell(cellRightWithoutBorder(formatMoney(item.totalPrice())));
        }
        document.add(table);
        document.add(new Paragraph(" ", normalFont));
        return 0;
    }

    private float writeTotals(final Document document, final Invoice invoice, final float fromY, final Locale locale) throws DocumentException {
        final float[] widths = {8, 40, 8, 10, 12, 12};
        final var table = new PdfPTable(widths);

        table.setWidthPercentage(100);

        table.addCell(cellWithTopBorder(" "));
        table.addCell(cellWithTopBorder(msg("invoice.pdf.netTotal", locale)));
        table.addCell(cellWithTopBorder(" "));
        table.addCell(cellWithTopBorder(" "));
        table.addCell(cellWithTopBorder(" "));
        table.addCell(cellRightWithTopBorder(formatMoney(invoice.totalNetPrice())));
        table.addCell(cellWithoutBorder(" "));
        table.addCell(cellWithoutBorder(msg("invoice.pdf.vat", new Object[]{formatDecimal(invoice.vatRate())}, locale)));
        table.addCell(cellWithoutBorder(" "));
        table.addCell(cellWithoutBorder(" "));
        table.addCell(cellWithoutBorder(" "));
        table.addCell(cellRightWithoutBorder(formatMoney(invoice.vatAbsolute())));
        table.addCell(cellWithoutBorder(" "));
        table.addCell(headerCellWithoutBorder(msg("invoice.pdf.totalGross", locale)));
        table.addCell(cellWithoutBorder(" "));
        table.addCell(cellWithoutBorder(" "));
        table.addCell(cellWithoutBorder(" "));
        table.addCell(headerCellRightWithoutBorder(formatMoney(invoice.totalGrossPrice())));
        table.addCell(cellWithoutBorder(" "));

        document.add(table);
        document.add(new Paragraph(" ", normalFont));
        return 0;
    }

    private void writeFinalNotes(final Document document, final String finalNotes, final Locale locale) throws DocumentException {
        if (finalNotes != null && !finalNotes.isBlank()) {
            document.add(new Paragraph(finalNotes, normalFont));
            document.add(new Paragraph(" "));

            return;
        }

        document.add(new Paragraph(getFinalNotes(locale), normalFont));
        document.add(new Paragraph(" "));
    }

    private String getFinalNotes(Locale l) {
        if (Locale.ENGLISH.getLanguage().equals(l.getLanguage())) {
            return "Terms of payment: Payment is due 30 days after receipt of invoice.\nPlease transfer the invoice amount to the account specified below, stating the invoice number.\n\nThank you for your order and the trust you have placed in us.\n\nBest regards,\nLucas Christian Müllner";
        }

        if ("es".equals(l.getLanguage())) {
            return "Condiciones de pago: El pago deberá efectuarse en un plazo de 30 días tras la recepción de la factura.\nLe rogamos que transfiera el importe de la factura a la cuenta que se indica a continuación, indicando el número de factura.\n\nGracias por su pedido y por la confianza depositada en nosotros.\n\nUn cordial saludo,\nLucas Christian Müllner";
        }

        throw new IllegalStateException("Unexpected value: " + l.getLanguage());
    }

    private void writeCompanyFooterTable(final Document document, final CompanyDetails company, final Locale locale) throws DocumentException {

        final float[] widths = {20, 20, 20, 20};
        final var table = new PdfPTable(widths);

        table.setSpacingBefore(21);
        table.setWidthPercentage(100);

        final var nestedT1 = new PdfPTable(1);
        nestedT1.addCell(nestedFooterCell(company.name()));
        nestedT1.addCell(nestedFooterCell(company.address().streetAndNumber()));
        nestedT1.addCell(nestedFooterCell(company.address().postalCode() + " " + company.address().city()));
        nestedT1.addCell(nestedFooterCell(company.address().countryName(locale)));
        final var c1 = new PdfPCell(nestedT1);
        c1.setBorderWidth(0);
        table.addCell(c1);

        final var nestedT2 = new PdfPTable(1);
        nestedT2.addCell(nestedFooterCell(msg("invoice.pdf.phone", locale) + " " + company.phone()));
        nestedT2.addCell(nestedFooterCell(msg("invoice.pdf.email", locale) + " " + company.email()));
        final var c2 = new PdfPCell(nestedT2);
        c2.setBorderWidth(0);
        table.addCell(c2);

        final var nestedT3 = new PdfPTable(1);
        nestedT3.addCell(nestedFooterCell(msg("invoice.pdf.placeOfJurisdiction", locale) + "\n" + company.placeOfJurisdiction()));
        nestedT3.addCell(nestedFooterCell(msg("invoice.pdf.companyId", locale) + " " + company.companyId()));
        nestedT3.addCell(nestedFooterCell(msg("invoice.pdf.ceoDirector", locale) + "\n" + company.ceoOrDirector()));
        nestedT3.addCell(nestedFooterCell(msg("invoice.pdf.vatId", locale) + " " + company.vatId()));
        final var c3 = new PdfPCell(nestedT3);
        c3.setBorderWidth(0);
        table.addCell(c3);

        final var bank = company.bankAccount();

        final var nestedT4 = new PdfPTable(1);
        nestedT4.addCell(nestedFooterCell(msg("invoice.pdf.bank", locale) + "\n" + bank.bankName()));
        nestedT4.addCell(nestedFooterCell(msg("invoice.pdf.accountOwner", locale) + "\n" + bank.accountOwner()));
        nestedT4.addCell(nestedFooterCell(msg("invoice.pdf.iban", locale) + "\n" + bank.iban()));
//        nestedT4.addCell(nestedFooterCell(msg("invoice.pdf.bic", locale) + "\n" + bank.bic()));
        nestedT4.addCell(nestedFooterCell(" "));

        final var c4 = new PdfPCell(nestedT4);
        c4.setBorderWidth(0);
        table.addCell(c4);

        table.addCell(cell(" "));

        final var footerParagraph = new Paragraph();
        footerParagraph.add(table);

        final var footer = new HeaderFooter(footerParagraph, false);
        footer.setBorderWidth(0);

        document.setFooter(footer);
    }

    private PdfPCell headerCell(final String text) {
        final var cell = new PdfPCell(new Phrase(text, headingFont));
        cell.setBorderWidth(0.5f);
        cell.setPadding(4);
        return cell;
    }

    private PdfPCell headerCellWithoutBorder(final String text) {
        final var c = headerCell(text);
        c.setBorderWidth(0f);
        c.setPadding(2);
        return c;
    }

    private PdfPCell headerCellWithBottomBorder(final String text) {
        final var c = headerCell(text);
        c.setBorderWidth(0f);
        c.setBorderWidthBottom(.5f);
        c.setPadding(2);
        return c;
    }

    private PdfPCell headerCellRight(final String text) {
        final var cell = new PdfPCell(new Phrase(text, headingFont));
        cell.setBorderWidth(0.5f);
        cell.setPadding(4);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return cell;
    }

    private PdfPCell headerCellRightWithoutBorder(final String text) {
        final var c = headerCellRight(text);
        c.setBorderWidth(0f);
        c.setPadding(2);
        return c;
    }

    private PdfPCell headerCellRightWithBottomBorder(final String text) {
        final var c = headerCellRight(text);
        c.setBorderWidth(0f);
        c.setBorderWidthBottom(.5f);
        c.setPadding(2);
        return c;
    }

    private PdfPCell cell(final String text) {
        final var cell = new PdfPCell(new Phrase(text, normalFont));
        cell.setBorderWidth(0.5f);
        cell.setPadding(4);
        return cell;
    }

    private PdfPCell cellWithoutBorder(final String text) {
        final var c = cell(text);
        c.setBorderWidth(0f);
        c.setPadding(2);
        return c;
    }

    private PdfPCell cellWithTopBorder(final String text) {
        final var c = cell(text);
        c.setBorderWidth(0f);
        c.setBorderWidthTop(0.5f);
        c.setPadding(2);
        return c;
    }

    private PdfPCell cellRight(final String text) {
        final var cell = new PdfPCell(new Phrase(text, normalFont));
        cell.setBorderWidth(0.5f);
        cell.setPadding(4);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return cell;
    }

    private PdfPCell cellRightWithoutBorder(final String text) {
        final var c = cellRight(text);
        c.setBorderWidth(0f);
        c.setPadding(2);
        return c;
    }

    private PdfPCell cellRightWithTopBorder(final String text) {
        final var c = cellRight(text);
        c.setBorderWidth(0f);
        c.setBorderWidthTop(0.5f);
        c.setPadding(2);
        return c;
    }

    private PdfPCell nestedFooterCell(final String text) {
        final var cell = new PdfPCell(new Phrase(text, smallFont));
        cell.setHorizontalAlignment(HorizontalAlignment.LEFT.getId());
        cell.setBorderWidth(0);
        cell.setPadding(2);
        cell.setSpaceCharRatio(0);
        return cell;
    }

    private static String formatDecimal(final BigDecimal value) {
        return value == null ? "" : value.stripTrailingZeros().toPlainString();
    }

    private static String formatMoney(final BigDecimal value) {
        return value == null ? "" : String.format("%.2f €", value);
    }
}

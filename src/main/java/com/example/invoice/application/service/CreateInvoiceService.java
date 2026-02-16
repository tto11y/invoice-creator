package com.example.invoice.application.service;

import com.example.invoice.application.port.PdfGenerationPort;
import com.example.invoice.domain.Invoice;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * Application service: orchestrates invoice creation and PDF generation.
 */
@Service
public class CreateInvoiceService {

    private final PdfGenerationPort pdfGenerationPort;

    public CreateInvoiceService(final PdfGenerationPort pdfGenerationPort) {
        this.pdfGenerationPort = pdfGenerationPort;
    }

    /**
     * Creates an invoice PDF from the given domain invoice in the given locale.
     *
     * @param invoice the invoice to render
     * @param locale  the locale for PDF labels (e.g. en, es)
     * @return PDF bytes
     */
    public byte[] createInvoicePdf(final Invoice invoice, final Locale locale) {
        return pdfGenerationPort.generate(invoice, locale);
    }
}

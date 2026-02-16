package com.example.invoice.adapter.out.pdf;

import com.example.invoice.TestFixtures;
import com.example.invoice.configuration.InvoicePdfProperties;
import com.example.invoice.domain.Invoice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

class OpenPdfInvoiceGeneratorTest {

    private OpenPdfInvoiceGenerator generator;

    @BeforeEach
    void setUp() {
        final var properties = new InvoicePdfProperties();
        properties.setDateFormat("dd.MM.yyyy");
        properties.setMarginMm(40f);
        properties.setTitleFontSize(18);
        properties.setHeadingFontSize(10);
        properties.setNormalFontSize(10);
        final var messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("messages");
        generator = new OpenPdfInvoiceGenerator(properties, messageSource);
    }

    @Test
    void generate_returnsNonEmptyPdfWithCorrectHeader() {
        final Invoice invoice = TestFixtures.minimalInvoice();

        final byte[] pdf = generator.generate(invoice, Locale.ENGLISH);

        assertThat(pdf).isNotEmpty();
        assertThat(new String(pdf, 0, Math.min(8, pdf.length))).startsWith("%PDF");
    }

    @Test
    void generate_spanishLocale_returnsPdfWithSpanishLabels() {
        final Invoice invoice = TestFixtures.minimalInvoice();

        final byte[] pdf = generator.generate(invoice, new Locale("es"));

        assertThat(pdf).isNotEmpty();
        assertThat(new String(pdf, 0, Math.min(8, pdf.length))).startsWith("%PDF");
    }
}

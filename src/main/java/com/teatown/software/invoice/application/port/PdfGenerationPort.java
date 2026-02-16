package com.teatown.software.invoice.application.port;

import com.teatown.software.invoice.domain.Invoice;

import java.util.Locale;

/**
 * Outbound port: generates a PDF document from an invoice.
 */
public interface PdfGenerationPort {

    /**
     * Generates a PDF byte array for the given invoice in the given locale.
     *
     * @param invoice the invoice domain object
     * @param locale  the locale for labels (e.g. en, es)
     * @return PDF content as bytes
     */
    byte[] generate(Invoice invoice, Locale locale);
}

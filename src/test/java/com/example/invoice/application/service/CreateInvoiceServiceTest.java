package com.example.invoice.application.service;

import com.example.invoice.TestFixtures;
import com.example.invoice.application.port.PdfGenerationPort;
import com.example.invoice.domain.Invoice;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateInvoiceServiceTest {

    @Mock
    private PdfGenerationPort pdfGenerationPort;

    @InjectMocks
    private CreateInvoiceService createInvoiceService;

    @Test
    void createInvoicePdf_delegatesToPortAndReturnsBytes() {
        final Invoice invoice = TestFixtures.minimalInvoice();
        final Locale locale = Locale.ENGLISH;
        final byte[] expectedPdf = new byte[]{1, 2, 3};
        when(pdfGenerationPort.generate(same(invoice), eq(locale))).thenReturn(expectedPdf);

        final byte[] result = createInvoiceService.createInvoicePdf(invoice, locale);

        assertThat(result).isSameAs(expectedPdf);
        verify(pdfGenerationPort).generate(invoice, locale);
    }
}

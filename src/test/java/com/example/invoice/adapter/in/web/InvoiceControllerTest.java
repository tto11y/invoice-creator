package com.example.invoice.adapter.in.web;

import com.example.invoice.TestFixtures;
import com.example.invoice.adapter.in.web.dto.CreateInvoiceRequestDto;
import com.example.invoice.application.service.CreateInvoiceService;
import com.example.invoice.domain.Invoice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class InvoiceControllerTest {

    private CreateInvoiceService createInvoiceService;
    private InvoiceRequestMapper requestMapper;
    private InvoiceController controller;

    @BeforeEach
    void setUp() {
        createInvoiceService = mock(CreateInvoiceService.class);
        requestMapper = mock(InvoiceRequestMapper.class);
        controller = new InvoiceController(createInvoiceService, requestMapper);
    }

    @Test
    void createInvoice_returnsPdfWithCorrectHeaders() {
        final CreateInvoiceRequestDto request = TestFixtures.createInvoiceRequestDto();
        final Invoice invoice = TestFixtures.minimalInvoice(); // invoice number "INV-1"
        final byte[] pdfBytes = new byte[]{1, 2, 3};
        when(requestMapper.toDomain(request)).thenReturn(invoice);
        when(createInvoiceService.createInvoicePdf(eq(invoice), any(Locale.class))).thenReturn(pdfBytes);

        final ResponseEntity<byte[]> response = controller.createInvoice(request, null, null);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION))
                .contains("invoice-INV-1.pdf");
        assertThat(response.getBody()).isEqualTo(pdfBytes);
        verify(requestMapper).toDomain(request);
        verify(createInvoiceService).createInvoicePdf(invoice, Locale.ENGLISH);
    }

    @Test
    void createInvoice_withLangParamEs_usesSpanish() {
        final CreateInvoiceRequestDto request = TestFixtures.createInvoiceRequestDto();
        final Invoice invoice = TestFixtures.minimalInvoice();
        final byte[] pdfBytes = new byte[]{1, 2, 3};
        when(requestMapper.toDomain(request)).thenReturn(invoice);
        when(createInvoiceService.createInvoicePdf(eq(invoice), any(Locale.class))).thenReturn(pdfBytes);

        controller.createInvoice(request, "es", null);

        verify(createInvoiceService).createInvoicePdf(invoice, new Locale("es"));
    }
}

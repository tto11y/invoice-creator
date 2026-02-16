package com.teatown.software.invoice.adapter.in.web;

import com.teatown.software.invoice.adapter.in.web.dto.CreateInvoiceRequestDto;
import com.teatown.software.invoice.application.service.CreateInvoiceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

/**
 * Inbound adapter: REST API for invoice creation.
 */
@RestController
@RequestMapping("/api/v1")
public class InvoiceController {

    private final CreateInvoiceService createInvoiceService;
    private final InvoiceRequestMapper requestMapper;

    public InvoiceController(final CreateInvoiceService createInvoiceService, final InvoiceRequestMapper requestMapper) {
        this.createInvoiceService = createInvoiceService;
        this.requestMapper = requestMapper;
    }

    /**
     * Creates an invoice PDF. Language for labels is taken from the {@code Accept-Language} header
     * or the {@code lang} query parameter (e.g. {@code en}, {@code es}). Defaults to English.
     */
    @PostMapping(value = "/invoices", consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/pdf")
    public ResponseEntity<byte[]> createInvoice(
            @Valid @RequestBody final CreateInvoiceRequestDto request,
            @RequestParam(name = "lang", required = false) final String langParam,
            @RequestHeader(value = "Accept-Language", required = false) final String acceptLanguage) {
        final var locale = resolveLocale(langParam, acceptLanguage);
        final var invoice = requestMapper.toDomain(request);
        final var pdf = createInvoiceService.createInvoicePdf(invoice, locale);

        final var filename = "invoice-" + invoice.invoiceNumber().replaceAll("[^a-zA-Z0-9.-]", "_") + ".pdf";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdf.length)
                .body(pdf);
    }

    private static Locale resolveLocale(final String langParam, final String acceptLanguage) {
        final String lang = langParam != null && !langParam.isBlank()
                ? langParam.trim().split("[_-]")[0].toLowerCase()
                : parseFirstLanguage(acceptLanguage);
        return "es".equals(lang) ? Locale.of("es") : Locale.ENGLISH;
    }

    private static String parseFirstLanguage(final String acceptLanguage) {
        if (acceptLanguage == null || acceptLanguage.isBlank()) {
            return "en";
        }
        final String first = acceptLanguage.split(",")[0].trim().split("[_-]")[0].toLowerCase();
        return first.isBlank() ? "en" : first;
    }
}

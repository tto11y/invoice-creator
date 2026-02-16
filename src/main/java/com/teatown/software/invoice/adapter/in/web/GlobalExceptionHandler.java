package com.teatown.software.invoice.adapter.in.web;

import com.teatown.software.invoice.application.exception.InvoicePdfException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * Maps exceptions to HTTP responses with a consistent problem-detail body.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(final MethodArgumentNotValidException ex) {
        final String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining("; "));
        if (log.isDebugEnabled()) {
            log.debug("Validation failed: {}", errors);
        }
        final var problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed: " + errors);
        problem.setTitle("Invalid Request");
        return problem;
    }

    @ExceptionHandler(InvoicePdfException.class)
    public ProblemDetail handleInvoicePdf(final InvoicePdfException ex) {
        log.error("Invoice PDF generation failed", ex);
        final var problem = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Invoice PDF could not be generated.");
        problem.setTitle("PDF Generation Error");
        return problem;
    }
}

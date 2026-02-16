package com.example.invoice.application.exception;

/**
 * Thrown when invoice PDF generation fails.
 */
public class InvoicePdfException extends RuntimeException {

    public InvoicePdfException(String message, Throwable cause) {
        super(message, cause);
    }
}

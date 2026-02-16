package com.teatown.software.invoice.domain;

/**
 * Supported units for invoice line items. Only hours ("h") are supported.
 */
public final class InvoiceItemUnit {

    /** Unit for hours. */
    public static final String HOURS = "h";

    /** Pattern for Jakarta validation: only "h" is allowed. */
    public static final String PATTERN = "^" + HOURS + "$";

    private InvoiceItemUnit() {}

    public static boolean isValid(String unit) {
        return HOURS.equals(unit);
    }
}

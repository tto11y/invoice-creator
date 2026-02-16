package com.teatown.software.invoice.domain;

import java.math.BigDecimal;

/**
 * Value object representing a single line item on an invoice.
 */
public record InvoiceItem(
        String description,
        BigDecimal quantity,
        String unit,
        BigDecimal unitPriceEuro,
        BigDecimal totalPrice
) {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String description;
        private BigDecimal quantity;
        private String unit;
        private BigDecimal unitPriceEuro;
        private BigDecimal totalPrice;

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder quantity(BigDecimal quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder unit(String unit) {
            this.unit = unit;
            return this;
        }

        public Builder unitPriceEuro(BigDecimal unitPriceEuro) {
            this.unitPriceEuro = unitPriceEuro;
            return this;
        }

        public Builder totalPrice(BigDecimal totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        public InvoiceItem build() {
            if (!InvoiceItemUnit.isValid(unit)) {
                throw new IllegalArgumentException("Unit must be '" + InvoiceItemUnit.HOURS + "' (hours), got: " + unit);
            }
            return new InvoiceItem(description, quantity, unit, unitPriceEuro, totalPrice);
        }
    }
}

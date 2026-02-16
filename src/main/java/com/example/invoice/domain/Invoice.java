package com.example.invoice.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Domain aggregate representing an invoice.
 */
public record Invoice(
        LocalDate invoiceDate,
        String invoiceNumber,
        LocalDate deliveryDate,
        LocalDate dueDate,
        List<InvoiceItem> items,
        BigDecimal totalNetPrice,
        BigDecimal vatRate,
        BigDecimal vatAbsolute,
        BigDecimal totalGrossPrice,
        String finalNotes,
        CompanyDetails companyDetails,
        Customer customer
) {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private LocalDate invoiceDate;
        private String invoiceNumber;
        private LocalDate deliveryDate;
        private LocalDate dueDate;
        private List<InvoiceItem> items = new ArrayList<>();
        private BigDecimal totalNetPrice;
        private BigDecimal vatRate;
        private BigDecimal vatAbsolute;
        private BigDecimal totalGrossPrice;
        private String finalNotes;
        private CompanyDetails companyDetails;
        private Customer customer;

        public Builder invoiceDate(LocalDate invoiceDate) {
            this.invoiceDate = invoiceDate;
            return this;
        }

        public Builder invoiceNumber(String invoiceNumber) {
            this.invoiceNumber = invoiceNumber;
            return this;
        }

        public Builder deliveryDate(LocalDate deliveryDate) {
            this.deliveryDate = deliveryDate;
            return this;
        }

        public Builder dueDate(LocalDate dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public Builder items(List<InvoiceItem> items) {
            this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
            return this;
        }

        public Builder addItem(InvoiceItem item) {
            this.items.add(item);
            return this;
        }

        public Builder totalNetPrice(BigDecimal totalNetPrice) {
            this.totalNetPrice = totalNetPrice;
            return this;
        }

        public Builder vatRate(BigDecimal vatRate) {
            this.vatRate = vatRate;
            return this;
        }

        public Builder vatAbsolute(BigDecimal vatAbsolute) {
            this.vatAbsolute = vatAbsolute;
            return this;
        }

        public Builder totalGrossPrice(BigDecimal totalGrossPrice) {
            this.totalGrossPrice = totalGrossPrice;
            return this;
        }

        public Builder finalNotes(String finalNotes) {
            this.finalNotes = finalNotes;
            return this;
        }

        public Builder companyDetails(CompanyDetails companyDetails) {
            this.companyDetails = companyDetails;
            return this;
        }

        public Builder customer(Customer customer) {
            this.customer = customer;
            return this;
        }

        public Invoice build() {
            return new Invoice(invoiceDate, invoiceNumber, deliveryDate, dueDate, items,
                    totalNetPrice, vatRate, vatAbsolute, totalGrossPrice, finalNotes,
                    companyDetails, customer);
        }
    }
}

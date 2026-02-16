package com.example.invoice.adapter.in.web.dto;

import com.example.invoice.adapter.in.web.validator.ReverseChargeValidation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@ReverseChargeValidation
public record CreateInvoiceRequestDto(
        @NotNull(message = "Invoice date is required")
        LocalDate invoiceDate,
        @NotBlank(message = "Invoice number is required")
        String invoiceNumber,
        @NotNull(message = "Delivery date is required")
        LocalDate deliveryDate,
        @NotNull(message = "Due date is required")
        LocalDate dueDate,
        @NotEmpty(message = "At least one invoice item is required")
        @Valid
        List<InvoiceItemDto> invoiceItems,
        @NotNull(message = "Total net price is required")
        @DecimalMin(value = "0", message = "Total net price must be non-negative")
        BigDecimal totalNetPrice,
        @NotNull(message = "VAT rate is required")
        @DecimalMax(value = "1", message = "VAT rate must be smaller than 1 (=100%)")
        @DecimalMin(value = "0", message = "VAT rate must be non-negative")
        BigDecimal vatRate,
        @NotNull(message = "VAT absolute is required")
        @DecimalMin(value = "0", message = "VAT absolute must be non-negative")
        BigDecimal vatAbsolute,
        @NotNull(message = "Total gross price is required")
        @DecimalMin(value = "0", message = "Total gross price must be non-negative")
        BigDecimal totalGrossPrice,
        String finalNotes,
        @NotNull(message = "Company details is required")
        @Valid
        CompanyDetailsDto companyDetails,
        @NotNull(message = "Customer is required")
        @Valid
        CustomerDto customer,
        Boolean reverseCharge
) {

        public static final String ATTR_REVERSE_CHARGE_NAME = "reverseCharge";
}

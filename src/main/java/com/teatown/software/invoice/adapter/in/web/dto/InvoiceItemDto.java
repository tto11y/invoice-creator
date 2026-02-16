package com.teatown.software.invoice.adapter.in.web.dto;

import com.teatown.software.invoice.domain.InvoiceItemUnit;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public record InvoiceItemDto(
        @NotBlank(message = "Description is required")
        String description,
        @NotNull(message = "Quantity is required")
        @DecimalMin(value = "0", inclusive = false, message = "Quantity must be positive")
        BigDecimal quantity,
        @NotBlank(message = "Unit is required")
        @Pattern(regexp = InvoiceItemUnit.PATTERN, message = "Unit must be '" + InvoiceItemUnit.HOURS + "' (hours)")
        String unit,
        @NotNull(message = "Unit price is required")
        @DecimalMin(value = "0", message = "Unit price must be non-negative")
        BigDecimal unitPriceEuro,
        @NotNull(message = "Total price is required")
        @DecimalMin(value = "0", message = "Total price must be non-negative")
        BigDecimal totalPrice
) {}

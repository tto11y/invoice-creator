package com.teatown.software.invoice.adapter.in.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CustomerDto(
        @NotBlank(message = "Customer company name is required")
        String companyName,
        @NotBlank(message = "Customer number is required")
        String customerNumber,
        String vatId,
        @NotBlank(message = "Contact is required")
        String contact,
        @NotNull(message = "Customer address is required")
        @Valid
        AddressDto address
) {}

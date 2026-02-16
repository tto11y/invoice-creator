package com.teatown.software.invoice.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;

public record BankAccountDto(
        @NotBlank(message = "Bank name is required")
        String bankName,
        @NotBlank(message = "Account owner is required")
        String accountOwner,
        @NotBlank(message = "IBAN is required")
        String iban,
        String bic
) {}

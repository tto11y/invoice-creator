package com.teatown.software.invoice.adapter.in.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CompanyDetailsDto(
        @NotBlank(message = "Company name is required")
        String name,
        @NotNull(message = "Address is required")
        @Valid
        AddressDto address,
        @NotBlank(message = "Phone is required")
        String phone,
        @NotBlank(message = "Email is required")
        @Email
        String email,
        @NotBlank(message = "Place of jurisdiction is required")
        String placeOfJurisdiction,
        @NotBlank(message = "Company ID is required")
        String companyId,
        @NotBlank(message = "CEO/Director is required")
        String ceoOrDirector,
        @NotNull(message = "Bank account is required")
        @Valid
        BankAccountDto bankAccount,
        @NotBlank(message = "VAT ID is required")
        String vatId
) {}

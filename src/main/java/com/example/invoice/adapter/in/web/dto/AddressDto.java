package com.example.invoice.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;

public record AddressDto(
        @NotBlank(message = "Street and number is required")
        String streetAndNumber,
        @NotBlank(message = "Postal code is required")
        String postalCode,
        @NotBlank(message = "City is required")
        String city,
        @NotBlank(message = "Country code is required")
        String countryCode
) {}

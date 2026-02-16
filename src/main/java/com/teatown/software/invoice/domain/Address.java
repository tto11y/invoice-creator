package com.teatown.software.invoice.domain;

import java.util.Locale;

/**
 * Value object representing an address (company or customer).
 */
public record Address(
        String streetAndNumber,
        String postalCode,
        String city,
        String countryCode
) {

    public String countryName(final Locale locale) {
        if (Locale.ENGLISH.getLanguage().equals(locale.getLanguage())) {
            return switch (countryCode) {
                case "AT" -> "Austria";
                case "ES" -> "Spain";
                default -> countryCode;
            };
        }

        if ("es".equals(locale.getLanguage())) {
            return switch (countryCode) {
                case "AT" -> "Austria";
                case "ES" -> "España";
                default -> countryCode;
            };
        }

        throw new IllegalStateException("Unexpected value: " + countryCode);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String streetAndNumber;
        private String postalCode;
        private String city;
        private String countryCode;

        public Builder streetAndNumber(String streetAndNumber) {
            this.streetAndNumber = streetAndNumber;
            return this;
        }

        public Builder postalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder countryCode(String countryCode) {
            this.countryCode = countryCode;
            return this;
        }

        public Address build() {
            return new Address(streetAndNumber, postalCode, city, countryCode);
        }
    }
}

package com.teatown.software.invoice.domain;

/**
 * Value object representing the customer (bill-to) details.
 */
public record Customer(
        String companyName,
        String customerNumber,
        String vatId,
        String contact,
        Address address
) {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String companyName;
        private String customerNumber;
        private String vatId;
        private String contact;
        private Address address;

        public Builder companyName(String companyName) {
            this.companyName = companyName;
            return this;
        }

        public Builder customerNumber(String customerNumber) {
            this.customerNumber = customerNumber;
            return this;
        }

        public Builder vatId(String vatId) {
            this.vatId = vatId;
            return this;
        }

        public Builder contact(String contact) {
            this.contact = contact;
            return this;
        }

        public Builder address(Address address) {
            this.address = address;
            return this;
        }

        public Customer build() {
            return new Customer(companyName, customerNumber, vatId, contact, address);
        }
    }
}

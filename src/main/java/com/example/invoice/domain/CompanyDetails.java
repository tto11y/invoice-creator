package com.example.invoice.domain;

/**
 * Value object representing the company (issuer) details.
 */
public record CompanyDetails(
        String name,
        Address address,
        String phone,
        String email,
        String placeOfJurisdiction,
        String companyId,
        String ceoOrDirector,
        BankAccount bankAccount,
        String vatId
) {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String name;
        private Address address;
        private String phone;
        private String email;
        private String placeOfJurisdiction;
        private String companyId;
        private String ceoOrDirector;
        private BankAccount bankAccount;
        private String vatId;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder address(Address address) {
            this.address = address;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder placeOfJurisdiction(String placeOfJurisdiction) {
            this.placeOfJurisdiction = placeOfJurisdiction;
            return this;
        }

        public Builder companyId(String companyId) {
            this.companyId = companyId;
            return this;
        }

        public Builder ceoOrDirector(String ceoOrDirector) {
            this.ceoOrDirector = ceoOrDirector;
            return this;
        }

        public Builder bankAccount(BankAccount bankAccount) {
            this.bankAccount = bankAccount;
            return this;
        }

        public Builder vatId(String vatId) {
            this.vatId = vatId;
            return this;
        }

        public CompanyDetails build() {
            return new CompanyDetails(name, address, phone, email, placeOfJurisdiction,
                    companyId, ceoOrDirector, bankAccount, vatId);
        }
    }
}

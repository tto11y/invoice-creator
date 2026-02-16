package com.example.invoice.domain;

/**
 * Value object representing a bank account.
 */
public record BankAccount(
        String bankName,
        String accountOwner,
        String iban,
        String bic
) {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String bankName;
        private String accountOwner;
        private String iban;
        private String bic;

        public Builder bankName(String bankName) {
            this.bankName = bankName;
            return this;
        }

        public Builder accountOwner(String accountOwner) {
            this.accountOwner = accountOwner;
            return this;
        }

        public Builder iban(String iban) {
            this.iban = iban;
            return this;
        }

        public Builder bic(String bic) {
            this.bic = bic;
            return this;
        }

        public BankAccount build() {
            return new BankAccount(bankName, accountOwner, iban, bic);
        }
    }
}

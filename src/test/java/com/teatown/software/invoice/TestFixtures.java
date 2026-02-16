package com.teatown.software.invoice;

import com.teatown.software.invoice.adapter.in.web.dto.*;
import com.teatown.software.invoice.domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Test fixtures for building minimal valid DTOs and domain objects.
 */
public final class TestFixtures {

    private TestFixtures() {}

    public static AddressDto addressDto() {
        return new AddressDto("Main St 1", "10115", "Berlin", "DE");
    }

    public static BankAccountDto bankAccountDto() {
        return new BankAccountDto("Bank", "Company GmbH", "DE89370400440532013000", "COBADEFFXXX");
    }

    public static CompanyDetailsDto companyDetailsDto() {
        return new CompanyDetailsDto(
                "Test Company GmbH",
                addressDto(),
                "+49 30 123456",
                "billing@test.com",
                "Berlin",
                "HRB 12345",
                "Jane Doe",
                bankAccountDto(),
                "DE123456789"
        );
    }

    public static CustomerDto customerDto() {
        return new CustomerDto("Client AG", "C-001", "DE987654321", "John Smith", addressDto());
    }

    public static InvoiceItemDto invoiceItemDto() {
        return new InvoiceItemDto(
                "Consulting",
                new BigDecimal("10"),
                "h",
                new BigDecimal("120"),
                new BigDecimal("1200")
        );
    }

    public static CreateInvoiceRequestDto createInvoiceRequestDto() {
        return new CreateInvoiceRequestDto(
                LocalDate.of(2025, 2, 14),
                "INV-2025-001",
                LocalDate.of(2025, 2, 14),
                LocalDate.of(2025, 3, 14),
                List.of(invoiceItemDto()),
                new BigDecimal("1200"),
                new BigDecimal("19"),
                new BigDecimal("228"),
                new BigDecimal("1428"),
                "Thank you.",
                companyDetailsDto(),
                customerDto()
        );
    }

    public static Invoice minimalInvoice() {
        final var address = Address.builder().streetAndNumber("Main 1").postalCode("10115").city("Berlin").countryCode("DE").build();
        final var bank = BankAccount.builder().bankName("Bank").accountOwner("Co").iban("DE89...").bic("COBADEFF").build();
        final var company = CompanyDetails.builder()
                .name("Co GmbH")
                .address(address)
                .phone("+49")
                .email("a@b.de")
                .placeOfJurisdiction("Berlin")
                .companyId("HRB 1")
                .ceoOrDirector("X")
                .bankAccount(bank)
                .vatId("DE1")
                .build();
        final var customer = Customer.builder()
                .companyName("Client AG")
                .customerNumber("C-1")
                .vatId("DE2")
                .contact("Y")
                .address(address)
                .build();
        final var item = InvoiceItem.builder()
                .description("Work")
                .quantity(new BigDecimal("1"))
                .unit("h")
                .unitPriceEuro(new BigDecimal("100"))
                .totalPrice(new BigDecimal("100"))
                .build();
        return Invoice.builder()
                .invoiceDate(LocalDate.of(2025, 1, 1))
                .invoiceNumber("INV-1")
                .deliveryDate(LocalDate.of(2025, 1, 1))
                .dueDate(LocalDate.of(2025, 2, 1))
                .items(List.of(item))
                .totalNetPrice(new BigDecimal("100"))
                .vatRate(new BigDecimal("19"))
                .vatAbsolute(new BigDecimal("19"))
                .totalGrossPrice(new BigDecimal("119"))
                .companyDetails(company)
                .customer(customer)
                .build();
    }
}

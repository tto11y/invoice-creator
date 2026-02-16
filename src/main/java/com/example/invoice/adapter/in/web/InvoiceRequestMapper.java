package com.example.invoice.adapter.in.web;

import com.example.invoice.adapter.in.web.dto.*;
import com.example.invoice.domain.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Maps HTTP request DTOs to domain objects.
 */
@Component
public class InvoiceRequestMapper {

    public Invoice toDomain(final CreateInvoiceRequestDto dto) {
        final List<InvoiceItem> domainItems = toDomainItems(dto.invoiceItems());

        final BigDecimal totalNetPrice;
        if (dto.totalNetPrice().compareTo(BigDecimal.ZERO) > 0) {
            totalNetPrice = dto.totalNetPrice();
        } else {
            totalNetPrice = domainItems.stream()
                    .map(InvoiceItem::totalPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        final BigDecimal vatAbsolute = dto.vatAbsolute().compareTo(BigDecimal.ZERO) > 0
                ? dto.vatAbsolute()
                : dto.vatRate().multiply(totalNetPrice);

        final BigDecimal totalGrossPrice = dto.totalGrossPrice().compareTo(BigDecimal.ZERO) > 0
               ? dto.totalGrossPrice()
               : totalNetPrice.add(vatAbsolute);

        return Invoice.builder()
                .invoiceDate(dto.invoiceDate())
                .invoiceNumber(dto.invoiceNumber())
                .deliveryDate(dto.deliveryDate())
                .dueDate(dto.dueDate())
                .items(domainItems)
                .totalNetPrice(totalNetPrice)
                .vatRate(dto.vatRate())
                .vatAbsolute(vatAbsolute)
                .totalGrossPrice(totalGrossPrice)
                .finalNotes(dto.finalNotes())
                .companyDetails(toDomainCompanyDetails(dto.companyDetails()))
                .customer(toDomainCustomer(dto.customer()))
                .build();
    }

    private List<InvoiceItem> toDomainItems(final List<InvoiceItemDto> items) {
        return items.stream()
                .map(this::toDomainItem)
                .toList();
    }

    private InvoiceItem toDomainItem(final InvoiceItemDto dto) {
        return InvoiceItem.builder()
                .description(dto.description())
                .quantity(dto.quantity())
                .unit(dto.unit())
                .unitPriceEuro(dto.unitPriceEuro())
                .totalPrice(
                        dto.totalPrice().compareTo(BigDecimal.ZERO) > 0
                        ? dto.totalPrice()
                        : dto.quantity().multiply(dto.unitPriceEuro())
                )
                .build();
    }

    private CompanyDetails toDomainCompanyDetails(final CompanyDetailsDto dto) {
        return CompanyDetails.builder()
                .name(dto.name())
                .address(toDomainAddress(dto.address()))
                .phone(dto.phone())
                .email(dto.email())
                .placeOfJurisdiction(dto.placeOfJurisdiction())
                .companyId(dto.companyId())
                .ceoOrDirector(dto.ceoOrDirector())
                .bankAccount(toDomainBankAccount(dto.bankAccount()))
                .vatId(dto.vatId())
                .build();
    }

    private Customer toDomainCustomer(final CustomerDto dto) {
        return Customer.builder()
                .companyName(dto.companyName())
                .customerNumber(dto.customerNumber())
                .vatId(dto.vatId())
                .contact(dto.contact())
                .address(toDomainAddress(dto.address()))
                .build();
    }

    private Address toDomainAddress(final AddressDto dto) {
        return Address.builder()
                .streetAndNumber(dto.streetAndNumber())
                .postalCode(dto.postalCode())
                .city(dto.city())
                .countryCode(dto.countryCode())
                .build();
    }

    private BankAccount toDomainBankAccount(final BankAccountDto dto) {
        return BankAccount.builder()
                .bankName(dto.bankName())
                .accountOwner(dto.accountOwner())
                .iban(dto.iban())
                .bic(dto.bic())
                .build();
    }
}

package com.teatown.software.invoice.adapter.in.web;

import com.teatown.software.invoice.TestFixtures;
import com.teatown.software.invoice.adapter.in.web.dto.CreateInvoiceRequestDto;
import com.teatown.software.invoice.domain.Invoice;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InvoiceRequestMapperTest {

    private final InvoiceRequestMapper mapper = new InvoiceRequestMapper();

    @Test
    void toDomain_mapsAllFields() {
        final CreateInvoiceRequestDto dto = TestFixtures.createInvoiceRequestDto();

        final Invoice invoice = mapper.toDomain(dto);

        assertThat(invoice.invoiceNumber()).isEqualTo("INV-2025-001");
        assertThat(invoice.invoiceDate()).isEqualTo(dto.invoiceDate());
        assertThat(invoice.totalNetPrice()).isEqualByComparingTo(dto.totalNetPrice());
        assertThat(invoice.totalGrossPrice()).isEqualByComparingTo(dto.totalGrossPrice());
        assertThat(invoice.items()).hasSize(1);
        assertThat(invoice.items().get(0).description()).isEqualTo("Consulting");
        assertThat(invoice.items().get(0).unit()).isEqualTo("h");
        assertThat(invoice.companyDetails().name()).isEqualTo("Test Company GmbH");
        assertThat(invoice.customer().companyName()).isEqualTo("Client AG");
    }
}

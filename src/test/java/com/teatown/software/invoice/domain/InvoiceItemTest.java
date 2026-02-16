package com.teatown.software.invoice.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InvoiceItemTest {

    @Test
    void build_withUnitHours_succeeds() {
        final InvoiceItem item = InvoiceItem.builder()
                .description("Work")
                .quantity(BigDecimal.ONE)
                .unit(InvoiceItemUnit.HOURS)
                .unitPriceEuro(new BigDecimal("100"))
                .totalPrice(new BigDecimal("100"))
                .build();

        assertThat(item.unit()).isEqualTo("h");
    }

    @Test
    void build_withInvalidUnit_throws() {
        assertThatThrownBy(() -> InvoiceItem.builder()
                .description("Work")
                .quantity(BigDecimal.ONE)
                .unit("kg")
                .unitPriceEuro(BigDecimal.ONE)
                .totalPrice(BigDecimal.ONE)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unit must be 'h'");
    }
}

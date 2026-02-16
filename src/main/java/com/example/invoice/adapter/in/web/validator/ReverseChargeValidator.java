package com.example.invoice.adapter.in.web.validator;


import com.example.invoice.adapter.in.web.dto.CreateInvoiceRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class ReverseChargeValidator implements ConstraintValidator<ReverseChargeValidation, CreateInvoiceRequestDto> {

    @Override
    public boolean isValid(final CreateInvoiceRequestDto createInvoiceRequestDto, final ConstraintValidatorContext context) {

        final Boolean reverseCharge = createInvoiceRequestDto.reverseCharge();

        if (reverseCharge == null) {
            context.buildConstraintViolationWithTemplate("Reverse charge is required")
                    .addPropertyNode(CreateInvoiceRequestDto.ATTR_REVERSE_CHARGE_NAME)
                    .addConstraintViolation();
            return false;
        }

        if (!reverseCharge) {
            return true;
        }

        final boolean vatAbsoluteIsZero = createInvoiceRequestDto.vatAbsolute().compareTo(BigDecimal.ZERO) == 0;
        final boolean vatRateIsZero = createInvoiceRequestDto.vatRate().compareTo(BigDecimal.ZERO) == 0;

        if (!vatAbsoluteIsZero || !vatRateIsZero) {
            context.buildConstraintViolationWithTemplate(
                            "VAT rate and VAT absolute must be 0 when it is a reverse charge invoice")
                    .addPropertyNode(CreateInvoiceRequestDto.ATTR_REVERSE_CHARGE_NAME)
                    .addConstraintViolation();

            return false;
        }

        return true;
    }

}

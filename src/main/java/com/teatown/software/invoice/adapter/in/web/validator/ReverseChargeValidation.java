package com.teatown.software.invoice.adapter.in.web.validator;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ReverseChargeValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ReverseChargeValidation {
    String message() default "did not validate";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};

}

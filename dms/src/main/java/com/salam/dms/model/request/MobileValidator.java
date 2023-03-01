package com.salam.dms.model.request;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneNumberValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MobileValidator {
    String message() default "{com.constraint.MobileValidation.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

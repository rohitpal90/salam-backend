package com.salam.dms.model.request;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DatValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DateValidator {
    String message() default "{com.constraint.DateValidation.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

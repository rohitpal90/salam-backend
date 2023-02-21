package com.salam.dms.model.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class DateValidator implements ConstraintValidator<ValidDate, String> {

    private DateTimeFormatter dateFormatter;
    private boolean checkPast;


    @Override
    public void initialize(ValidDate annotation) {
        this.dateFormatter = DateTimeFormatter.ofPattern(annotation.pattern());
        this.checkPast = annotation.checkPast();
    }

    @Override
    public boolean isValid(String date, ConstraintValidatorContext context) {
        try {
            var parsedDate = validateDate(date);
            return Objects.nonNull(parsedDate) &&
                    (checkPast && isPastDate(parsedDate));
        } catch (Exception e) {
            return false;
        }
    }

    private LocalDate validateDate(String date) {
        return LocalDate.parse(date, dateFormatter);
    }

    private boolean isPastDate(LocalDate date) {
        return date.isBefore(LocalDate.now());
    }
}

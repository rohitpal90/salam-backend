package com.salam.dms.model.request;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatValidator implements ConstraintValidator<DateValidator, String> {

    private static final String SIMPLE_FORMAT = "dd-MM-yyyy";
    private static final String SIMPLE_FORMAT1 = "dd/MM/yyyy";
    private static final String DATE_FORMAT = "dd/MM/yyyy";

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        try {
            return (validDate(s) && notFutureDate(s));
        } catch (Exception e) {
            return false;
        }
    }

    private boolean validDate(String s) {
        String regex = "^(0?[1-9]|[12][0-9]|3[01])-(0?[1-9]|1[012])-[0-9]{4}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(s);
        if (m.matches()) {
            return true;
        }
        return false;
    }

    private boolean notFutureDate(String s) throws ParseException {
        SimpleDateFormat sdf1 = new SimpleDateFormat(SIMPLE_FORMAT);
        SimpleDateFormat sdf2 = new SimpleDateFormat(SIMPLE_FORMAT1);
        String ds2 = sdf2.format(sdf1.parse(s));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        LocalDate givenDate = LocalDate.parse(ds2, formatter);
        LocalDate todayDate = LocalDate.now();
        if (todayDate.isBefore(givenDate)) {
            return false;
        }
        return true;
    }
}

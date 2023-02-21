package com.salam.dms.model.request;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber.CountryCodeSource;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class NumberValidator implements ConstraintValidator<MobileValidator, String> {

    PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
    private static final String DEFAULT_REGION = "SA";


    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        try {
            PhoneNumber phone = phoneNumberUtil.parse(s, CountryCodeSource.UNSPECIFIED.name());
            return phoneNumberUtil.isValidNumberForRegion(phone, DEFAULT_REGION);
        } catch (NumberParseException e) {
            return false;
        }
    }
}

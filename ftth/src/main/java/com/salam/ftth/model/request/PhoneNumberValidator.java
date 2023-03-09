package com.salam.ftth.model.request;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber.CountryCodeSource;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class PhoneNumberValidator implements ConstraintValidator<MobileValidator, String> {

    private static final PhoneNumberUtil PHONEUTIL = PhoneNumberUtil.getInstance();
    private static final String DEFAULT_REGION = "SA"; // TODO: from env


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return isValidPhone(value);
    }

    public static boolean isValidPhone(String value) {
        try {
            PhoneNumber phone = PHONEUTIL.parse(value, CountryCodeSource.UNSPECIFIED.name());
            return PHONEUTIL.isValidNumberForRegion(phone, DEFAULT_REGION);
        } catch (NumberParseException e) {
            return false;
        }
    }
}

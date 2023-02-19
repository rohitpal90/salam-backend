package com.salam.dms.model.request;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.SneakyThrows;



public class NumberValidator implements ConstraintValidator<MobileValidator,String> {

    PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();


    @SneakyThrows
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        Phonenumber.PhoneNumber phone = phoneNumberUtil.parse(s,
                Phonenumber.PhoneNumber.CountryCodeSource.UNSPECIFIED.name());
        return phoneNumberUtil.isValidNumberForRegion(phone,"SA");
    }
}

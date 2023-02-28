package com.salam.dms.services;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.salam.dms.config.exception.AppError;
import com.salam.dms.db.entity.User;
import com.salam.dms.model.request.DealerEmailLogin;
import com.salam.dms.repos.UserRepository;
import eu.fraho.spring.securityJwt.base.service.TotpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.salam.dms.config.exception.AppErrors.*;

@Service
public class UserService {

    PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    @Autowired
    UserRepository userRepository;
    @Autowired
    TotpService totpService;

    private static final String DEFAULT_REGION = "SA";


    public Optional<User> checkLogin(String username) throws NumberParseException {
        String email = null;
        String phoneNo = null;
        if(isMobileNo(username)){
            PhoneNumber phone = phoneNumberUtil.parse(username, PhoneNumber.CountryCodeSource.UNSPECIFIED.name());
            boolean isPhoneNumber = phoneNumberUtil.isValidNumberForRegion(phone, DEFAULT_REGION);
            if(isPhoneNumber) {
                phoneNo = username;
            }
            else{
                throw AppError.create(NUMBER_FORMAT_ERROR);
            }
        }
        else{
            if(isValidEmail(username)) {
                email = username;
            }
            else{
                throw AppError.create(INVALID_EMAIL);
            }
        }
        System.out.println(phoneNo);
        System.out.println(userRepository.findByEmail(email,phoneNo));
        return userRepository.findByEmail(email,phoneNo);
    }

    private boolean isValidEmail(String username) {
        String  regexPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    private boolean isMobileNo(String str) {
        int len = str.length();
        for (int i = 1; i < len; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public void LoginCheck(DealerEmailLogin login) throws NumberParseException {
        var userOpt = this.checkLogin(login.getUsername());
        if (userOpt.isEmpty()) {
            throw AppError.create(USER_NOT_FOUND);
        }

        var user = userOpt.get();
        System.out.print(totpService.generateSecret());
        int randomPin   =(int)(Math.random()*900000)+100000;
        user.setTotp(String.valueOf(randomPin));
        //dealer.setTotp("1234");

        userRepository.save(user);
    }

}

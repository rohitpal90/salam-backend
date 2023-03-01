package com.salam.dms.services;

import com.salam.dms.config.auth.OtpService;
import com.salam.dms.config.exception.AppError;
import com.salam.dms.db.entity.User;
import com.salam.dms.model.request.PhoneNumberValidator;
import com.salam.dms.repos.UserRepository;
import eu.fraho.spring.securityJwt.base.dto.AuthenticationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import java.util.Optional;

import static com.salam.dms.config.exception.AppErrors.USER_NOT_FOUND;

@Service
@AllArgsConstructor
public class UserService {
    final UserRepository userRepository;
    final OtpService otpService;
    final Validator validator;


    public Optional<User> checkLogin(String username) {
        return userRepository.findUserByPrincipal(username);
    }

    public boolean isPhoneLogin(String username) {
        return PhoneNumberValidator.isValidPhone(username);
    }

    @Transactional
    public void performLoginStep1(AuthenticationRequest request) {
        var username = request.getUsername();
        var userOpt = this.checkLogin(username);
        if (userOpt.isEmpty()) {
            throw AppError.create(USER_NOT_FOUND);
        }

        if (isPhoneLogin(username)) {
            var secret = otpService.generateSecret();
            String totp = otpService.generateCode(secret);
            // send otp to phone

            var user = userOpt.get();

            // OTP: 8720
            // TODO: change this
            user.setTotp("LJLDNVG4JZREUHWJXENUS37F5ADXOZJG");
            userRepository.save(user);
        }
    }
}
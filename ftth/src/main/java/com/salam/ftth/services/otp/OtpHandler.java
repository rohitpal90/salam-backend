package com.salam.ftth.services.otp;

import com.salam.ftth.config.auth.UserDetailService;
import com.salam.ftth.config.exception.AppError;
import com.salam.ftth.db.entity.User;
import com.salam.ftth.model.request.OtpOpType;
import com.salam.ftth.model.request.OtpVerifyRequest;
import com.salam.ftth.services.OtpService;
import com.salam.ftth.services.UserService;
import eu.fraho.spring.securityJwt.base.dto.JwtUser;

import java.util.Arrays;
import java.util.Optional;

import static com.salam.ftth.config.exception.AppErrors.INVALID_OTP;

public abstract class OtpHandler<T> {
    private final OtpService otpService;
    private final UserService userService;

    public abstract T handle(OtpVerifyRequest verifyRequest);
    public abstract OtpOpType getType();

    public OtpHandler(OtpService otpService, UserService userService) {
        this.otpService = otpService;
        this.userService = userService;
    }

    public OtpService getOtpService() {
        return otpService;
    }

    public UserService getUserService() {
        return userService;
    }

    private String getTotpSecret(JwtUser user) {
        var totpSecretOpt = user.getTotpSecret();
        if (totpSecretOpt.isEmpty()) {
            throw AppError.create(INVALID_OTP);
        }

        var totpSecret = totpSecretOpt.get();
        var secretTokens = Arrays.stream(totpSecret.split("_")).toList();
        if (!secretTokens.get(0).equals(getType().name())) {
            throw AppError.create(INVALID_OTP);
        }

        return secretTokens.get(1);
    }

    private void verifyCode(JwtUser user, String code) {
        var secret = getTotpSecret(user);
        if (!otpService.verifyCode(secret, code)) {
            throw AppError.create(INVALID_OTP);
        }
    }

    public User verifyOtpRequest(OtpVerifyRequest request) {
        var userOpt = checkLogin(request.getUsername());
        if (userOpt.isEmpty()) {
            throw AppError.create(INVALID_OTP);
        }

        var user = userOpt.get();
        var userDetails = UserDetailService.buildJwtUser(user);

        verifyCode(userDetails, request.getOtp());
        return user;
    }

    public Optional<User> checkLogin(String username) {
        return userService.checkLogin(username);
    }
}

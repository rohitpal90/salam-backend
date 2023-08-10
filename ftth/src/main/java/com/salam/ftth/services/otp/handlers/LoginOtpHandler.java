package com.salam.ftth.services.otp.handlers;

import com.salam.ftth.config.auth.LoginService;
import com.salam.ftth.config.auth.UserDetailService;
import com.salam.ftth.model.request.OtpOpType;
import com.salam.ftth.model.request.OtpVerifyRequest;
import com.salam.ftth.services.OtpService;
import com.salam.ftth.services.UserService;
import com.salam.ftth.services.otp.OtpHandler;
import eu.fraho.spring.securityJwt.base.dto.AuthenticationResponse;
import org.springframework.stereotype.Component;

@Component
public class LoginOtpHandler extends OtpHandler<AuthenticationResponse> {
    private final LoginService loginService;

    public LoginOtpHandler(LoginService loginService, UserService userService, OtpService otpService) {
        super(otpService, userService);
        this.loginService = loginService;
    }

    @Override
    public AuthenticationResponse handle(OtpVerifyRequest request) {
        var user = verifyOtpRequest(request);
        return loginService.login(UserDetailService.buildJwtUser(user));
    }

    @Override
    public OtpOpType getType() {
        return OtpOpType.LOGIN;
    }
}

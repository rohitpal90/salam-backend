package com.salam.ftth.services.otp.handlers;

import com.salam.ftth.config.auth.LoginService;
import com.salam.ftth.config.auth.UserDetailService;
import com.salam.ftth.db.entity.User;
import com.salam.ftth.model.request.OtpOpType;
import com.salam.ftth.model.request.OtpVerifyRequest;
import com.salam.ftth.services.CustomerService;
import com.salam.ftth.services.OtpService;
import com.salam.ftth.services.UserService;
import com.salam.ftth.services.otp.OtpHandler;
import eu.fraho.spring.securityJwt.base.dto.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RegisterOtpHandler extends OtpHandler<AuthenticationResponse> {
    private final CustomerService customerService;
    private final LoginService loginService;


    @Autowired
    public RegisterOtpHandler(OtpService otpService,
                              CustomerService customerService,
                              UserService userService,
                              LoginService loginService) {
        super(otpService, userService);
        this.customerService = customerService;
        this.loginService = loginService;
    }

    @Override
    public AuthenticationResponse handle(OtpVerifyRequest request) {
        var user = verifyOtpRequest(request);
        customerService.verifyUser(user);

        return loginService.login(UserDetailService.buildJwtUser(user));
    }

    @Override
    public Optional<User> checkLogin(String username) {
        var userService = getUserService();
        return userService.checkLogin(username, false);
    }

    @Override
    public OtpOpType getType() {
        return OtpOpType.REGISTER;
    }
}

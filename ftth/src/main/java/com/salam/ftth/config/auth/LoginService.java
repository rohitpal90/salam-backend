package com.salam.ftth.config.auth;

import com.nimbusds.jose.JOSEException;
import com.salam.ftth.config.exception.AppError;
import com.salam.ftth.model.request.LoginRequest;
import com.salam.ftth.model.request.OtpOpType;
import com.salam.ftth.services.OtpService;
import com.salam.ftth.services.UserService;
import eu.fraho.spring.securityJwt.base.dto.AccessToken;
import eu.fraho.spring.securityJwt.base.dto.AuthenticationResponse;
import eu.fraho.spring.securityJwt.base.dto.JwtUser;
import eu.fraho.spring.securityJwt.base.service.JwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.salam.ftth.config.exception.AppErrors.BAD_CREDENTIALS;

@Component
@RequiredArgsConstructor
public class LoginService {

    private final JwtTokenService jwtTokenService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserService userService;
    private final OtpService otpService;


    private boolean validatePassword(LoginRequest request, JwtUser userDetails) {
        return passwordEncoder.matches(request.getPassword(), userDetails.getPassword());
    }

    @Transactional
    public void checkLogin(LoginRequest request) {
        var userOpt = userService.checkLogin(request.getUsername());
        if (userOpt.isEmpty()) {
            throw AppError.create(BAD_CREDENTIALS);
        }

        var user = userOpt.get();
        var userDetails = UserDetailService.buildJwtUser(user);
        if (!validatePassword(request, userDetails)) {
            throw AppError.create(BAD_CREDENTIALS);
        }

        var secret = otpService.generateSecret();
        var totp = otpService.generateCode(secret);

        // TODO: send otp request

        user.setTotp(String.format("%s_%s", OtpOpType.LOGIN.name(), secret));
        userService.save(user);
    }

    @SneakyThrows
    public AuthenticationResponse login(JwtUser userDetails) throws AuthenticationException {
        var authentication = new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        AccessToken accessToken;

        try {
            accessToken = jwtTokenService.generateToken(userDetails);
        } catch (JOSEException e) {
            throw new BadCredentialsException("Token generation failed");
        }

        var refreshToken = jwtTokenService.generateRefreshToken(userDetails);
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}

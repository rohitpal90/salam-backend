package com.salam.ftth.config.auth;

import com.nimbusds.jose.JOSEException;
import com.salam.ftth.config.exception.AppError;
import com.salam.ftth.model.request.LoginRequest;
import eu.fraho.spring.securityJwt.base.dto.AccessToken;
import eu.fraho.spring.securityJwt.base.dto.AuthenticationResponse;
import eu.fraho.spring.securityJwt.base.dto.JwtUser;
import eu.fraho.spring.securityJwt.base.service.JwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.salam.ftth.config.exception.AppErrors.BAD_CREDENTIALS;
import static com.salam.ftth.config.exception.AppErrors.INVALID_CAPTCHA;

@Component
@RequiredArgsConstructor
public class LoginService {

    private final JwtTokenService jwtTokenService;
    private final UserDetailService userDetailService;
    private final BCryptPasswordEncoder passwordEncoder;


    public Optional<UserDetails> getUserDetails(LoginRequest request) {
        var userDetails = (JwtUser) userDetailService.loadUserByUsername(request.getUsername());

        if (!validatePassword(request, userDetails)) {
            return Optional.empty();
        }

        return Optional.of(userDetails);
    }

    private boolean validatePassword(LoginRequest request, JwtUser userDetails) {
        return passwordEncoder.matches(request.getPassword(), userDetails.getPassword());
    }

    @Transactional
    public void checkLogin(LoginRequest request) {
        var userDetailsOpt = getUserDetails(request);
        if (userDetailsOpt.isEmpty()) {
            throw AppError.create(BAD_CREDENTIALS);
        }

        // send otp request
    }

    @SneakyThrows
    public AuthenticationResponse login(LoginRequest request) throws AuthenticationException {
        var authentication = new TestingAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        );

        var userDetailsOpt = getUserDetails(request);
        if (userDetailsOpt.isEmpty()) {
            throw AppError.create(BAD_CREDENTIALS);
        }

        var userDetails = (JwtUser) userDetailsOpt.get();
        if (!request.getCaptchaCode().equals("1234")) { // TODO: change value
            throw AppError.create(INVALID_CAPTCHA);
        }

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

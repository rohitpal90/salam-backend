package com.salam.dms.config.auth;

import com.nimbusds.jose.JOSEException;
import com.salam.dms.config.exception.AppError;
import com.salam.dms.services.UserService;
import eu.fraho.spring.securityJwt.base.dto.AccessToken;
import eu.fraho.spring.securityJwt.base.dto.AuthenticationRequest;
import eu.fraho.spring.securityJwt.base.dto.AuthenticationResponse;
import eu.fraho.spring.securityJwt.base.dto.JwtUser;
import eu.fraho.spring.securityJwt.base.service.JwtTokenService;
import eu.fraho.spring.securityJwt.base.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.salam.dms.config.exception.AppErrors.BAD_CREDENTIALS;

@Component
@RequiredArgsConstructor
public class PasswordIgnoringLoginService implements LoginService {

    private final JwtTokenService jwtTokenService;
    private final UserDetailService userDetailService;
    private final UserService userService;
    private final OtpService otpService;
    private final BCryptPasswordEncoder passwordEncoder;

    @SneakyThrows
    @Override
    public AuthenticationResponse checkLogin(AuthenticationRequest request)
            throws AuthenticationException {
        var authentication = new TestingAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        );

        var userDetailsOpt = getUserDetails(request);
        if (userDetailsOpt.isEmpty()) {
            throw AppError.create(BAD_CREDENTIALS);
        }

        var userDetails = (JwtUser) userDetailsOpt.get();
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

    protected boolean isTotpOk(AuthenticationRequest request, JwtUser user) {
        if (user.getTotpSecret().isEmpty()) {
            return false;
        }

        var secret = user.getTotpSecret().get();
        return otpService.verifyCode(secret, request.getTotp().orElse(-1));
    }

    protected Optional<UserDetails> getUserDetails(AuthenticationRequest request) {
        var userDetails = (JwtUser) userDetailService.loadUserByUsername(request.getUsername());

        if (validatePhoneLogin(request, userDetails)) {
            return Optional.of(userDetails);
        }

        if (validatePassword(request, userDetails)) {
            return Optional.of(userDetails);
        }

        return Optional.empty();
    }

    private boolean validatePassword(AuthenticationRequest request, JwtUser userDetails) {
        return passwordEncoder.matches(request.getPassword(), userDetails.getPassword());
    }

    /**
     * when login is otp based
     */
    private boolean validatePhoneLogin(AuthenticationRequest request, JwtUser userDetails) {
        var username = request.getUsername();
        if (!isPhoneLogin(username)) {
            return false;
        }

        return userDetails.isEnabled() && this.isTotpOk(request, userDetails);
    }

    private boolean isPhoneLogin(String username) {
        return userService.isPhoneLogin(username);
    }

}

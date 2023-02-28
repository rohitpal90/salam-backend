package com.salam.dms.config.auth;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.nimbusds.jose.JOSEException;
import com.salam.dms.config.exception.AppError;
import eu.fraho.spring.securityJwt.base.controller.LoginRestController;
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
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.salam.dms.config.exception.AppErrors.*;

@Component
@RequiredArgsConstructor
public class PasswordIgnoringLoginService implements LoginService {

    private final JwtTokenService jwtTokenService;
    private final UserDetailService userDetailService;
    PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
    private static final String DEFAULT_REGION = "SA";

    @SneakyThrows
    @Override
    public AuthenticationResponse checkLogin(AuthenticationRequest authRequest)
            throws AuthenticationException {
        var authentication = new TestingAuthenticationToken(
                authRequest.getUsername(),
                authRequest.getPassword()
        );

        Optional<UserDetails> optionalUserDetails = getUserName(authRequest);
        if (optionalUserDetails.isPresent()) {
            var userDetails = (JwtUser)optionalUserDetails.get();
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
        }  else {
            throw AppError.create(INVALID_OTPT);
        }

    }

    protected boolean isTotpOk(Integer totp, JwtUser userDetails) {
        return userDetails.getTotpSecret()
                .map((secret) ->
                        Optional.ofNullable(totp)
                                .map((code) -> secret.equals(code.toString()))
                                .orElse(false))
                .orElse(true);
    }

    protected Optional<UserDetails> getUserName(AuthenticationRequest authRequest) throws NumberParseException {
        if(isMobileNumber(authRequest.getUsername())){
            Phonenumber.PhoneNumber phone = phoneNumberUtil.parse(authRequest.getUsername(), Phonenumber.PhoneNumber.CountryCodeSource.UNSPECIFIED.name());
            boolean isPhoneNumber = phoneNumberUtil.isValidNumberForRegion(phone, DEFAULT_REGION);
            if(isPhoneNumber) {
                 var userDetails = (JwtUser) userDetailService.loadUserByUsername(authRequest.getUsername());
                 if (userDetails.isEnabled() && userDetails.isApiAccessAllowed() &&
                         this.isTotpOk((Integer) authRequest.getTotp().orElse((Integer) null), userDetails)) {
                     return Optional.of(userDetails);
                 }
            }
            else{
                throw AppError.create(NUMBER_FORMAT_ERROR);
            }

        }
        else{
            if(isValidEmail(authRequest.getUsername())) {
                var userDetails = (JwtUser) userDetailService.loadUserByUsername(authRequest.getUsername());
                if (userDetails.getPassword().equals(authRequest.getPassword()) && userDetails.isApiAccessAllowed() &&
                        this.isTotpOk((Integer) authRequest.getTotp().orElse((Integer) null), userDetails)) {
                    return Optional.of(userDetails);
                }
            }
            else{
                throw AppError.create(INVALID_EMAIL_PASSWORD);
            }
        }
       return Optional.empty();
    }

    private boolean isValidEmail(String username) {
        String  regexPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    private boolean isMobileNumber(String str) {
        int len = str.length();
        for (int i = 1; i < len; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

}

package com.salam.dms.config.auth;

import com.nimbusds.jose.JOSEException;
import eu.fraho.spring.securityJwt.base.controller.LoginRestController;
import eu.fraho.spring.securityJwt.base.dto.AccessToken;
import eu.fraho.spring.securityJwt.base.dto.AuthenticationRequest;
import eu.fraho.spring.securityJwt.base.dto.AuthenticationResponse;
import eu.fraho.spring.securityJwt.base.dto.JwtUser;
import eu.fraho.spring.securityJwt.base.service.JwtTokenService;
import eu.fraho.spring.securityJwt.base.service.LoginService;
import eu.fraho.spring.securityJwt.base.service.TotpService;
import eu.fraho.spring.securityJwt.base.service.TotpServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PasswordIgnoringLoginService implements LoginService {

    private final JwtTokenService jwtTokenService;
    private final UserDetailsService userDetailsService;

    @Override
    public AuthenticationResponse checkLogin(AuthenticationRequest authRequest)
            throws AuthenticationException {
        var authentication = new TestingAuthenticationToken(
                authRequest.getUsername(),
                authRequest.getPassword()
        );

        var userDetails = (JwtUser) userDetailsService.loadUserByUsername(authRequest.getUsername());
        if (userDetails.isApiAccessAllowed() &&
                this.isTotpOk((Integer) authRequest.getTotp().orElse((Integer) null), userDetails)) {

            SecurityContextHolder.getContext().setAuthentication(authentication);
            AccessToken accessToken;

            try {
                accessToken = jwtTokenService.generateToken(userDetails);
            } catch (JOSEException e) {
                throw new BadCredentialsException("Token generation failed");
            }

            return AuthenticationResponse.builder().accessToken(accessToken).build();
        }  else {
            throw new BadCredentialsException("Invalid TOTP or insufficient access rights");
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

}

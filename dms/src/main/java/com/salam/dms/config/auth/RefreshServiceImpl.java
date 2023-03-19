package com.salam.dms.config.auth;


import com.nimbusds.jose.JOSEException;
import com.salam.dms.config.exception.AppError;
import eu.fraho.spring.securityJwt.base.dto.AccessToken;
import eu.fraho.spring.securityJwt.base.dto.AuthenticationResponse;
import eu.fraho.spring.securityJwt.base.dto.JwtUser;
import eu.fraho.spring.securityJwt.base.dto.RefreshToken;
import eu.fraho.spring.securityJwt.base.exceptions.FeatureNotConfiguredException;
import eu.fraho.spring.securityJwt.base.service.JwtTokenService;
import eu.fraho.spring.securityJwt.base.service.RefreshService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.salam.dms.config.exception.AppErrors.*;

@Component
@AllArgsConstructor
public class RefreshServiceImpl implements RefreshService {

    private final JwtTokenService jwtTokenService;

    @Override
    public AuthenticationResponse checkRefresh(String token) throws AuthenticationException {
        if (!this.jwtTokenService.isRefreshTokenSupported()) {
            throw new FeatureNotConfiguredException("Refresh token support is disabled");
        } else if (token == null) {
            throw AppError.create(BAD_CREDENTIALS);
        } else {
            Optional<JwtUser> jwtUser = this.jwtTokenService.useRefreshToken(token);
            if (jwtUser.isEmpty()) {
                throw AppError.create(BAD_TOKEN);
            } else {
                JwtUser userDetails = jwtUser.get();
                if (!userDetails.isApiAccessAllowed()) {
                    throw AppError.create(INSUFFICIENT_ACCESS);
                } else {
                    AccessToken accessToken;
                    try {
                        accessToken = this.jwtTokenService.generateToken(userDetails);
                    } catch (JOSEException var6) {
                        throw new BadCredentialsException("Token generation failed");
                    }

                    RefreshToken refreshToken = this.jwtTokenService.generateRefreshToken(userDetails);
                    return AuthenticationResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
                }
            }
        }
    }
}

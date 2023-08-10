package com.salam.ftth.config.auth;

import eu.fraho.spring.securityJwt.base.config.RefreshCookieProperties;
import eu.fraho.spring.securityJwt.base.config.TokenCookieProperties;
import eu.fraho.spring.securityJwt.base.controller.RefreshRestController;
import eu.fraho.spring.securityJwt.base.service.JwtTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@AutoConfigureAfter({SecurityJwtBaseAutoConfiguration.class})
public class SecurityJwtNoRefreshStoreAutoConfiguration {

    @Bean
    public RefreshRestController refreshRestController(JwtTokenService jwtTokenService,
                                                       TokenCookieProperties tokenCookieProperties,
                                                       RefreshCookieProperties refreshCookieProperties) {
        log.debug("Register RefreshRestController");
        RefreshRestController controller = new RefreshRestController();
        controller.setJwtTokenService(jwtTokenService);
        controller.setTokenCookieProperties(tokenCookieProperties);
        controller.setRefreshCookieProperties(refreshCookieProperties);
        return controller;
    }
}

package com.salam.ftth.config.auth;

import eu.fraho.spring.securityJwt.base.config.*;
import eu.fraho.spring.securityJwt.base.dto.JwtUser;
import eu.fraho.spring.securityJwt.base.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
@AutoConfigureBefore({SecurityAutoConfiguration.class})
@EnableMethodSecurity(
        securedEnabled = true
)
public class SecurityJwtBaseAutoConfiguration {

    @Bean
    public TotpProperties totpProperties() {
        log.debug("Register TotpProperties");
        return new TotpProperties();
    }

    @Bean
    public TotpService totpService() {
        log.debug("Register TotpService");
        TotpServiceImpl totpService = new TotpServiceImpl();
        totpService.setTotpProperties(this.totpProperties());
        return totpService;
    }

    @Bean
    public TokenProperties tokenProperties() {
        log.debug("Register TokenProperties");
        return new TokenProperties();
    }

    @Bean
    public RefreshProperties refreshProperties() {
        log.debug("Register RefreshProperties");
        return new RefreshProperties();
    }

    @Bean
    public JwtTokenService jwtTokenService() {
        log.debug("Register JwtTokenService");
        JwtTokenServiceImpl jwtTokenService = new JwtTokenServiceImpl();
        jwtTokenService.setTokenProperties(this.tokenProperties());
        jwtTokenService.setRefreshProperties(this.refreshProperties());
        jwtTokenService.setTokenCookieProperties(this.tokenCookieProperties());
        jwtTokenService.setTokenHeaderProperties(this.tokenHeaderProperties());
        jwtTokenService.setRefreshCookieProperties(this.refreshCookieProperties());
        jwtTokenService.setJwtUser(this::jwtUser);
        return jwtTokenService;
    }

    @Bean
    public TokenCookieProperties tokenCookieProperties() {
        log.debug("Register TokenCookieProperties");
        return new TokenCookieProperties();
    }

    @Bean
    public TokenHeaderProperties tokenHeaderProperties() {
        log.debug("Register TokenHeaderProperties");
        return new TokenHeaderProperties();
    }

    @Bean
    public RefreshCookieProperties refreshCookieProperties() {
        log.debug("Register RefreshCookieProperties");
        return new RefreshCookieProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    public PasswordEncoder passwordEncoder() {
        log.debug("Register BCryptPasswordEncoder");
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Scope("prototype")
    @ConditionalOnMissingBean
    public JwtUser jwtUser() {
        log.debug("Register JwtUser");
        return new JwtUser();
    }

    @Bean
    @ConditionalOnMissingBean
    public RefreshService refreshService(JwtTokenService jwtTokenService) {
        RefreshServiceImpl service = new RefreshServiceImpl();
        service.setJwtTokenService(jwtTokenService);
        return service;
    }
}

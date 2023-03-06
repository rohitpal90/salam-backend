package com.salam.dms.model.request;

import eu.fraho.spring.securityJwt.base.dto.AuthenticationRequest;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;

public class AuthenticationRequestPOJOBuilder {
    @NotNull
    String username;
    String password;
    Integer totp;

    public AuthenticationRequestPOJOBuilder() {}

    public AuthenticationRequestPOJOBuilder username(String username) {
        this.username = username;
        return this;
    }

    public AuthenticationRequestPOJOBuilder password(String password) {
        this.password = password;
        return this;
    }

    public AuthenticationRequestPOJOBuilder totp(Integer totp) {
        this.totp = totp;
        return this;
    }

    public AuthenticationRequest build() {
        return AuthenticationRequest.builder()
                .username(username)
                .password(Optional.ofNullable(password).orElse(""))
                .totp(totp)
                .build();
    }
}

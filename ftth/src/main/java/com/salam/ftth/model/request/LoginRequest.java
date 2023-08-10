package com.salam.ftth.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @MobileValidator
    private String username;

    @NotBlank
    private String password;
}

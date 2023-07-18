package com.salam.ftth.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(groups = {LoginRequestStep1.class, LoginRequestStep2.class})
    private String username;

    @NotBlank(groups = {LoginRequestStep1.class, LoginRequestStep2.class})
    private String password;

    @NotBlank(groups = {LoginRequestStep2.class})
    @Pattern(regexp = "(^[0-9]{4})", message = "{com.constraint.OtpValidation.message}")
    private String captchaCode;

    public interface LoginRequestStep1 {

    }

    public interface LoginRequestStep2 {

    }
}

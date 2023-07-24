package com.salam.ftth.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class OtpVerifyRequest {

    @MobileValidator
    private String username;

    @NotBlank
    @Pattern(regexp = "(^[0-9]{4})", message = "{com.constraint.OtpValidation.message}")
    private String otp;
}

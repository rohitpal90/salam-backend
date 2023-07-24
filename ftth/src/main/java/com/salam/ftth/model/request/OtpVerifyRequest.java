package com.salam.ftth.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class OtpVerifyRequest {

    @MobileValidator
    private String username;

    @Schema(type = "string", allowableValues = {"register", "login"})
    @NotBlank
    @Pattern(regexp="^(register|login)$", message="invalid operation type, possible values: register,login")
    private String operation;

    @NotBlank
    @Pattern(regexp = "(^[0-9]{4})", message = "{com.constraint.OtpValidation.message}")
    private String otp;
}

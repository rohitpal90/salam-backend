package com.salam.ftth.model.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class VerifyCustomerRequest {

    @NotEmpty(message = "{com.constraint.FieldEmpty.message}")
    @Pattern(regexp = "(^[0-9]{4})",message = "{com.constraint.OtpValidation.message}")
    private String otp;
}

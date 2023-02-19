package com.salam.dms.model.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VerifyCustomerRequest {
    @NotEmpty(message = "mobileOtp cannot be null")
    @Pattern(regexp = "(^[0-9]{6})",message = "Otp format is wrong")
    private String mobileOtp;
}

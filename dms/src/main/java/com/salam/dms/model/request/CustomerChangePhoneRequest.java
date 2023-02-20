package com.salam.dms.model.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CustomerChangePhoneRequest {
    @NotEmpty(message = "{com.constraint.FieldEmpty.message}")
    @MobileValidator
    private String mobile;
}

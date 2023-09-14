package com.salam.ftth.model.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PaymentInitRequest {

    @NotEmpty(message = "{com.constraint.FieldEmpty.message}")
    private String returnUrl;
}

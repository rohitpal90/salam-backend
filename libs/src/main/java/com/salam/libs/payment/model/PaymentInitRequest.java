package com.salam.libs.payment.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentInitRequest {
    private String returnUrl;
}

package com.salam.libs.payment.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
public class PaymentRequest {
    private String description;
    private BigDecimal amount;
    private Map<String, Object> attrs;
}

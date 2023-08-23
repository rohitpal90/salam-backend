package com.salam.libs.feign.payment.client.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class InvoiceRefundRequest {
    private BigDecimal amount;
    private String reason;
}

package com.salam.libs.feign.payment.client.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class InvoiceInfoResponse {
    private String invoiceId;
    private String applicationId;
    private String description;
    private String paymentUrl;
    private BigDecimal amount;
    private String status;
    private String paymentMethod;
    private Map<String, Object> metadata;
}

package com.salam.libs.feign.payment.client.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
public class InvoiceRequest {
    private String applicationId;
    private String description;
    private BigDecimal amount;
    private Map<String, Object> metadata;
}

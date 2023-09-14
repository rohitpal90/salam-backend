package com.salam.libs.feign.payment.client.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class InvoiceRequest {
    private String applicationId;
    private String description;
    private BigDecimal amount;
    private boolean auto;
    private String returnUrl;

    private String expiresAt;
    private String metadata;
    private User user;


    @Data
    @Builder
    public static class User {
        private String id;
        private String name;
        private String phone;
    }
}

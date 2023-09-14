package com.salam.libs.feign.payment.client.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Data
public class InvoiceInfoResponse {
    private String id;
    private String url;
    private String description;
    private String returnUrl;
    private BigDecimal amount;
    private String status;
    private Date expiresAt;
    private String metadata;
    private List<Payment> payments = Collections.emptyList();
    private Date createdAt;
    private Date updatedAt;


    @Data
    public static class Payment {
        private String id;
        private BigDecimal amount;
        private String status;
        private String source;
        private String method;
        private Date createdAt;
        private Date updatedAt;
    }
}

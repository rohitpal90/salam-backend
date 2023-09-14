package com.salam.libs.payment.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PaymentRequest {
    private String description;
    private BigDecimal amount;
    private Attrs attrs;

    @Data
    @Builder
    public static class Attrs {
        private String name;
        private String email;
        private String mobile;
        private String state;
        private String reqId;
    }
}

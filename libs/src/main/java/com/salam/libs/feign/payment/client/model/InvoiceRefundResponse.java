package com.salam.libs.feign.payment.client.model;

import lombok.Data;

@Data
public class InvoiceRefundResponse {
    private String refundId;
    private String status;
}

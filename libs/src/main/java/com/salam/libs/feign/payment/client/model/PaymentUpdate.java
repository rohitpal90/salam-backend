package com.salam.libs.feign.payment.client.model;

import lombok.Data;

@Data
public class PaymentUpdate {
    private String id;
    private String type;
    private String secretToken;
    private String applicationName;
    private InvoiceInfoResponse invoice;
}

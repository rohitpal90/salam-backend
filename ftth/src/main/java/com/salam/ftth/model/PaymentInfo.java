package com.salam.ftth.model;


import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
@Builder
public class PaymentInfo {
    private String invoiceId;
    private Date createdAt;
    private Date updatedAt;
    private String status;
    private String lastUpdateId;
    private Map<String, Object> meta;

}

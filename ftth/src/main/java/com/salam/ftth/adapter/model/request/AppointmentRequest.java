package com.salam.ftth.adapter.model.request;

import lombok.Data;

@Data
public class AppointmentRequest {
    private Integer addressId;
    private String startDate;
    private String accessMode;
    private Integer offerId;
    private String subsPlanId;
    private Integer subsEventId;
}

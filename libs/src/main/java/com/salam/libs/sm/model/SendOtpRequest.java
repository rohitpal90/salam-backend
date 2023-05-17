package com.salam.libs.sm.model;
import lombok.Data;


@Data
public class SendOtpRequest {

    private String customerId;
    private String operatorId;
    private String reason;
    private String packageName;
    private String language;


}

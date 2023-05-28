package com.salam.libs.feign.elm.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SendOtpRequest {
    private String customerId;
    private String operatorId;
    private String reason;
    private String packageName;
    private String language;
}

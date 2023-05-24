package com.salam.libs.feign.elm.model;

import lombok.Data;


@Data
public class SendOtpResponseSuccessSalamResponse {
    private Boolean success;
    private String traceId;
    private SendOtpResponse data;
}

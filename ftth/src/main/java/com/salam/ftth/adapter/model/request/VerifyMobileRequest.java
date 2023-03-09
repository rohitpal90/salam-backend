package com.salam.ftth.adapter.model.request;

import lombok.Data;

@Data
public class VerifyMobileRequest {
    private String otp;
    private String requestId;
}

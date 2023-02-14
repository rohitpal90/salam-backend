package com.salam.dms.adapter.model.request;

import lombok.Data;

@Data
public class VerifyMobileRequest {
    private String otp;
    private String requestId;
}

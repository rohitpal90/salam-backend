package com.salam.ftth.adapter.model.response;

import lombok.Data;

@Data
public class VerifyByEmailResponse {
    private String currentTime;
    private String captchaCode;
    private String status;
}

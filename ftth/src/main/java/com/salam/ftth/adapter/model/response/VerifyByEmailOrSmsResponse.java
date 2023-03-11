package com.salam.ftth.adapter.model.response;

import lombok.Data;

@Data
public class VerifyByEmailOrSmsResponse {
    private String currentTime;
    private String captchaCode;
    private String status;
}

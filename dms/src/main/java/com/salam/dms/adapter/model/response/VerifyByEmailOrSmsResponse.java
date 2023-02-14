package com.salam.dms.adapter.model.response;

import lombok.Data;

@Data
public class VerifyByEmailOrSmsResponse {
    private String currentTime;
    private String captchaCode;
    private String status;
}

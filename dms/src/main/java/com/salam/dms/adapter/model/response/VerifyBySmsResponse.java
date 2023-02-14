package com.salam.dms.adapter.model.response;

import lombok.Data;

@Data
public class VerifyBySmsResponse {
    private String currentTime;
    private String captchaCode;
    private String status;
}

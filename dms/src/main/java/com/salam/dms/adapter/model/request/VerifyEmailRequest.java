package com.salam.dms.adapter.model.request;

import lombok.Data;

@Data
public class VerifyEmailRequest {
    private String email;
    private String subsEventId;
    private String subsPlanId;
}

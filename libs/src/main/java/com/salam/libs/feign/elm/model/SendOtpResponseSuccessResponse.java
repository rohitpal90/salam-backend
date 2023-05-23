package com.salam.libs.feign.elm.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class SendOtpResponseSuccessResponse {

    @JsonProperty("success")
    private Boolean success = null;

    @JsonProperty("traceId")
    private String traceId = null;

    @JsonProperty("data")
    private SendOtpResponse data = null;
}

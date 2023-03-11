package com.salam.ftth.adapter.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CustomerResponse {

    @JsonProperty("accessToken")
    private String accessToken;

    @JsonProperty("expirationDate")
    private String expirationDate;

    @JsonProperty("status")
    private String status;

    @JsonProperty("errorCode")
    private String errorCode;

    @JsonProperty("errorMessage")
    private String errorMessage;

}

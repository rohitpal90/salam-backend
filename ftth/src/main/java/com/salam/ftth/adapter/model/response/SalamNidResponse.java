package com.salam.ftth.adapter.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.salam.ftth.adapter.model.Cust;

public class SalamNidResponse {
    @JsonProperty("cust")
    private Cust cust;

    @JsonProperty("isExist")
    private String isExist;

    @JsonProperty("status")
    private String status;
}

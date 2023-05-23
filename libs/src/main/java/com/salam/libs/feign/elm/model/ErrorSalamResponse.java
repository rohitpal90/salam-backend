package com.salam.libs.feign.elm.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ErrorSalamResponse {

    @JsonProperty("success")
    private Boolean success = null;

    @JsonProperty("traceId")
    private String traceId = null;

    @JsonProperty("message")
    private String message = null;

    @JsonProperty("error")
    private String error = null;

    @JsonProperty("errors")
    private Map<String, List<String>> errors = null;
}

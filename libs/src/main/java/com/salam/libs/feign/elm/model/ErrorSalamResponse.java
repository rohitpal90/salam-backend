package com.salam.libs.feign.elm.model;

import lombok.Data;

import java.util.List;
import java.util.Map;


@Data
public class ErrorSalamResponse {
    private Boolean success;
    private String traceId;
    private String message;
    private String error;
    private Map<String, List<String>> errors;
}

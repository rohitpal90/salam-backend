package com.salam.libs.feign.elm.model;

import lombok.Data;


@Data
public class SalamSuccessResponse<T> {
    private Boolean success;
    private String traceId;
    private T data;
}

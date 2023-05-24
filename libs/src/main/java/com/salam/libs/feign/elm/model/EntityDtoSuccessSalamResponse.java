package com.salam.libs.feign.elm.model;

import lombok.Data;


@Data
public class EntityDtoSuccessSalamResponse {
    private Boolean success;
    private String traceId;
    private EntityDto data;
}

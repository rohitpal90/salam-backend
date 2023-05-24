package com.salam.libs.feign.elm.model;

import lombok.Data;

import java.util.List;


@Data
public class AddressDtoListSuccessSalamResponse {
    private Boolean success;
    private String traceId;
    private List<AddressDto> data;
}

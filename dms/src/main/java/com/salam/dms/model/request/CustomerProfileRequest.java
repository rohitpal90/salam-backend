package com.salam.dms.model.request;


import lombok.Data;

@Data
public class CustomerProfileRequest {
    private String nid;
    private String mobile;
    private String odbPlateNumber;
}

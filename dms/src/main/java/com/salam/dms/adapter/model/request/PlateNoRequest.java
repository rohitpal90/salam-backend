package com.salam.dms.adapter.model.request;

import lombok.Data;

@Data
public class PlateNoRequest {
    private String plateNumber;
    private String offerId;
    private String subsPlanId;
    private String provider;
    private String seeker;
    private String needCheckOdb;
}

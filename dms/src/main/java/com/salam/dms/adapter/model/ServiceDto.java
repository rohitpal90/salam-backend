package com.salam.dms.adapter.model;

import lombok.Data;

@Data
public class ServiceDto {
    public int offerId;
    public String offerCode;
    public String offerName;
    public String comments;
    public String defaultFlag;
    public String servType;
    public String operationType;
    public String timeUnit;
    public String cycleQuantity;
    public String serviceAttrDtoList;
}

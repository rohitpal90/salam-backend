package com.salam.ftth.adapter.model;

import lombok.Data;

@Data
public class PricePlanDto {
    public int offerId;
    public String offerCode;
    public String offerName;
    public String comments;
    public String defaultFlag;
    public String servType;
    public String operationType;
    public String effType;
    public String effDate;
    public String expDate;
    public String agreementPeriod;
    public String timeUnit;
    public String cycleQuantity;
    public String pricePlanAttrDtoList;
}

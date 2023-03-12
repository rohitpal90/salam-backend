package com.salam.ftth.adapter.model;

import lombok.Data;

@Data
public class GoodsDto {
    public int offerId;
    public String offerCode;
    public String offerName;
    public String comments;
    public String defaultFlag;
    public String operationType;
    public String quantity;
    public String modelCode;
    public String timeUnit;
    public String cycleQuantity;
    public String goodsSn;
    public String goodsAttrDtoList;
}

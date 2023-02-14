package com.salam.dms.adapter.model.response;

import com.salam.dms.adapter.model.GoodsDto;
import com.salam.dms.adapter.model.PricePlanDto;
import com.salam.dms.adapter.model.ServiceDto;
import lombok.Data;

import java.util.List;

@Data
public class PlansMockResponse {
    public String subsPlanCode;
    public String subsPlanName;
    public String effDate;
    public String expDate;
    public int subsPlanId;
    public int indepProdSpecId;
    public String effType;
    public String comments;
    public int salePrice;
    public String timeUnit;
    public String cycleQuantity;
    public String indepProdCode;
    public String indepProdName;
    public String servType;
    public String servTypeName;
    public String paidFlag;
    public String otc;
    public String mrc;
    public List<GoodsDto> goodsDtoList;
    public List<ServiceDto> serviceDtoList;
    public List<PricePlanDto> pricePlanDtoList;
    public String vobbSubsPlanName;
    public String speed;
}

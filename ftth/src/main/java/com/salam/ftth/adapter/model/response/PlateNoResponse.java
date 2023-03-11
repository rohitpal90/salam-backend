package com.salam.ftth.adapter.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.salam.ftth.adapter.model.AccSchema;
import com.salam.ftth.adapter.model.Address;
import lombok.Data;

import java.util.List;

@Data
public class PlateNoResponse {
    @JsonProperty("FeasibilityId")
    public int feasibilityId;

    @JsonProperty("Address")
    public Address address;

    @JsonProperty("ResultCode")
    public int resultCode;

    @JsonProperty("AccSchemaList")
    public List<AccSchema> accSchemaList;

    @JsonProperty("ResultDesc")
    public String resultDesc;

    @JsonProperty("IsSuccess")
    public boolean isSuccess;

    @JsonProperty("Status")
    public String status;
}

package com.salam.dms.adapter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Address {

    @JsonProperty("AreaId")
    public String areaId;

    @JsonProperty("Province")
    public String province;

    @JsonProperty("City")
    public String city;

    @JsonProperty("Latitude")
    public String latitude;

    @JsonProperty("HouseNo")
    public String houseNo;

    @JsonProperty("Distinct")
    public String distinct;

    @JsonProperty("PostCode")
    public String postCode;

    @JsonProperty("Region")
    public String region;

    @JsonProperty("AddressId")
    public int addressId;

    @JsonProperty("Longitude")
    public String longitude;

}

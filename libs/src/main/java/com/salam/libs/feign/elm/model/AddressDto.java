package com.salam.libs.feign.elm.model;

import lombok.Data;


@Data
public class AddressDto {
    private Integer unitNumber;
    private String streetName;
    private String district;
    private String city;
    private String locationCoordinates;
    private Integer buildingNumber;
    private Integer additionalNumber;
    private Integer postCode;
}

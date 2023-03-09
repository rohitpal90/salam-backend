package com.salam.ftth.adapter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Cust {
    @JsonProperty("certTypeName")
    private String certTypeName;

    @JsonProperty("defLangId")
    private String defLangId;

    @JsonProperty("cert")
    private Cert cert;

    @JsonProperty("certTypeId")
    private Float certTypeId;

    @JsonProperty("needUpload")
    private String needUpload;

    @JsonProperty("thirdName")
    private String thirdName;

    @JsonProperty("industryId")
    private String industryId;

    @JsonProperty("occupationId")
    private String occupationId;

    @JsonProperty("custTypeName")
    private String custTypeName;

    @JsonProperty("state")
    private String state;

    @JsonProperty("createPartyType")
    private String createPartyType;

    @JsonProperty("certNbr")
    private String certNbr;

    @JsonProperty("netType")
    private String netType;

    @JsonProperty("contactFixNbr")
    private String contactFixNbr;

    @JsonProperty("zipcode")
    private String zipcode;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("phoneNumber")
    private String phoneNumber;

    @JsonProperty("birthdayDay")
    private String birthdayDay;

    @JsonProperty("custType")
    private String custType;

    @JsonProperty("custCreditGradeId")
    private String custCreditGradeId;

    @JsonProperty("operationType")
    private String operationType;

    @JsonProperty("pwd")
    private String pwd;

    @JsonProperty("stdAddrId")
    private String stdAddrId;

    @JsonProperty("updateDate")
    private String updateDate;

    @JsonProperty("routingId")
    private Float routingId;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("impGradeId")
    private String impGradeId;

    @JsonProperty("fourName")
    private String fourName;

    @JsonProperty("partyType")
    private String partyType;

    @JsonProperty("religionId")
    private String religionId;

    @JsonProperty("valid")
    private Boolean valid;

    @JsonProperty("custTitleId")
    private String custTitleId;

    @JsonProperty("custId")
    private Float custId;

    @JsonProperty("email")
    private String email;

    @JsonProperty("secondName")
    private String secondName;

    @JsonProperty("address")
    private String address;

    @JsonProperty("comments")
    private String comments;

    @JsonProperty("custAttrValueList")
    private String custAttrValueList;

    @JsonProperty("vatNo")
    private String vatNo;

    @JsonProperty("createPartyCode")
    private String createPartyCode;

    @JsonProperty("certId")
    private Float certId;

    @JsonProperty("custName")
    private String custName;

    @JsonProperty("custCode")
    private String custCode;

    @JsonProperty("spId")
    private Float spId;

    @JsonProperty("parentId")
    private String parentId;

    @JsonProperty("areaId")
    private Float areaId;

    @JsonProperty("createdDate")
    private String createdDate;

    @JsonProperty("partyCode")
    private String partyCode;

    @JsonProperty("stateDate")
    private String stateDate;
}

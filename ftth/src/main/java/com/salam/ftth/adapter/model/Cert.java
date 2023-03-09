package com.salam.ftth.adapter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Cert {

    @JsonProperty("valid")
    private Boolean valid;

    @JsonProperty("effDate")
    private String effDate;

    @JsonProperty("certNbr")
    private String certNbr;

    @JsonProperty("certTypeId")
    private Float certTypeId;

    @JsonProperty("operationType")
    private String operationType;

    @JsonProperty("certId")
    private Float certId;

    @JsonProperty("certAddress")
    private String certAddress;

    @JsonProperty("issueDate")
    private String issueDate;

    @JsonProperty("spId")
    private Float spId;

    @JsonProperty("expDate")
    private String expDate;

    @JsonProperty("issueOrg")
    private String issueOrg;

}

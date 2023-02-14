package com.salam.dms.adapter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Model {

    @JsonProperty("ModelName")
    public String modelName;

    @JsonProperty("ModelId")
    public String modelId;

    @JsonProperty("ModelCode")
    public String modelCode;

    @JsonProperty("TemplateId")
    public String templateId;

    @JsonProperty("VendorCode")
    public String vendorCode;

}

package com.salam.dms.adapter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
public class AccSchema {

    @JsonProperty("ModelList")
    public ArrayList<Model> modelList;

    @JsonProperty("AccMode")
    public String accMode;

}

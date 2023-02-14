package com.salam.dms.adapter.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PlanMetaInfo {

    private String downloadSpeed;
    private String name;
    private String planType;
    private String price;
    private String uploadSpeed;
    private String description;
    private String openAccessId;
    private String category;
    private List<Addons> addons;
}

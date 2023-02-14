package com.salam.dms.model.request;

import lombok.Data;

@Data
public class PlanFilterRequest {
    private Long planId;
    private String category;
    private String type;
}

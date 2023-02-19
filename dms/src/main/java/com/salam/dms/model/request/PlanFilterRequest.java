package com.salam.dms.model.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PlanFilterRequest {
    @NotEmpty(message = "planId is mandatory")
    private Long planId;
    @NotEmpty(message = "category is mandatory")
    private String category;
    @NotEmpty(message = "type is mandatory")
    private String type;
}

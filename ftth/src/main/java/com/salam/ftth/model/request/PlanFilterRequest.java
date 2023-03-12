package com.salam.ftth.model.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PlanFilterRequest {
    @NotEmpty(message = "{com.constraint.FieldEmpty.message}")
    private Long planId;
    @NotEmpty(message = "{com.constraint.FieldEmpty.message}")
    private String category;
    @NotEmpty(message = "{com.constraint.FieldEmpty.message}")
    private String type;
}

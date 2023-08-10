package com.salam.ftth.model.request;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springdoc.core.annotations.ParameterObject;

@Data
@ParameterObject
public class PlanFilterRequest {

    @NotEmpty(message = "{com.constraint.FieldEmpty.message}")
    @Parameter(example = "1903")
    private Long planId;

    @NotEmpty(message = "{com.constraint.FieldEmpty.message}")
    @Parameter(example = "ftth")
    private String category;

    @NotEmpty(message = "{com.constraint.FieldEmpty.message}")
    @Parameter(example = "prepaid")
    private String type;
}

package com.salam.dms.model.request;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class CustomerProfileRequest {
    @NotEmpty(message = "{com.constraint.FieldEmpty.message}")
    @Pattern(regexp = "^[1|2]{1}[0-9]{9}$", message = "{com.constraint.NationalIdValidation.message}")
    private String nid;
    @NotEmpty(message = "{com.constraint.FieldEmpty.message}")
    @MobileValidator
    private String mobile;
    @NotEmpty(message = "{com.constraint.FieldEmpty.message}")
    private String odbPlateNumber;
    @NotEmpty(message = "{com.constraint.FieldEmpty.message}")
    @Pattern(regexp = "^(0?[1-9]|[12][0-9]|3[01])-(0?[1-9]|1[012])-[0-9]{4}$",
    message = "{com.constraint.DateValidation.message}")
    private String dob;
}

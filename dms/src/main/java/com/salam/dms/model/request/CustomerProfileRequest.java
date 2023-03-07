package com.salam.dms.model.request;


import com.salam.dms.model.validators.ValidDate;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class CustomerProfileRequest {
    @NotEmpty(message = "{com.constraint.FieldEmpty.message}")
    @Pattern(regexp = "^[1|2]{1}[0-9]{9}$", message = "{com.constraint.NationalIdValidation.message}")
    private String id;
    @NotEmpty(message = "{com.constraint.FieldEmpty.message}")
    @MobileValidator
    private String mobile;
    @NotEmpty(message = "{com.constraint.FieldEmpty.message}")
    private String odbPlateNumber;
    @NotEmpty(message = "{com.constraint.FieldEmpty.message}")
    @ValidDate(pattern = "dd-MM-yyyy", checkPast = true)
    private String dob;
    @Range(min = -90L, max = 90L)
    private Double lat;
    @Range(min = -180L, max = 180L)
    private Double lng;
}

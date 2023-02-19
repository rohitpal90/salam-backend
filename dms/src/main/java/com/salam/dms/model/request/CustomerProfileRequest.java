package com.salam.dms.model.request;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CustomerProfileRequest {
    @NotEmpty(message = "National id is mandatory")
    @Pattern(regexp = "^[1|2]{1}[0-9]{9}$", message = "Id format is wrong")
    private String nid;
    @NotEmpty(message = "mobile number is mandatory")
    @MobileValidator
    private String mobile;
    @NotEmpty(message = "odbPlateNumber is mandatory")
    private String odbPlateNumber;
}

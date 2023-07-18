package com.salam.ftth.model.request;

import com.salam.ftth.model.validators.ValidDate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterRequest {

    @Pattern(regexp="^(CITIZEN|EXPAT)$", message="invalid id type, possible values: CITIZEN,EXPAT")
    private String idType;

    @NotEmpty(message = "{com.constraint.FieldEmpty.message}")
    @Pattern(regexp = "^[1|2]{1}[0-9]{9}$", message = "{com.constraint.NationalIdValidation.message}")
    private String id;

    @NotEmpty(message = "{com.constraint.FieldEmpty.message}")
    @MobileValidator
    private String mobile;

    private String fullName;

    @Email
    private String email;

    @NotEmpty(message = "{com.constraint.FieldEmpty.message}")
    @ValidDate(pattern = "dd-MM-yyyy")
    private String dob;

    private String username;
    private String password;
    private String confirmPassword;
}

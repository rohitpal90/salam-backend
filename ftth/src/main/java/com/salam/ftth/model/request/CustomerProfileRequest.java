package com.salam.ftth.model.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.util.List;

@Data
public class CustomerProfileRequest {

    @NotEmpty(message = "{com.constraint.FieldEmpty.message}")
    @Pattern(regexp = "^[1|2]{1}[0-9]{9}$",
            message = "{com.constraint.NationalIdValidation.message}",
            groups = CustomProfileRequestGroup.class)
    private String id;

    @NotEmpty(message = "{com.constraint.FieldEmpty.message}")
    @Pattern(regexp = "^[0-9]{2}-[0-9]{4}$", message = "{com.constraint.DateFormat.message}")
    private String dob;

    @NotEmpty(message = "{com.constraint.FieldEmpty.message}")
    @MobileValidator(groups = ContactInfoRequestGroup.class)
    private String mobile;

    @Email(groups = ContactInfoRequestGroup.class)
    private String email;

    @NotEmpty(message = "{com.constraint.FieldEmpty.message}", groups = LocationRequestGroup.class)
    private String odbPlateNumber;

    @Range(min = -90L, max = 90L, groups = LocationRequestGroup.class)
    private Double lat;

    @Range(min = -180L, max = 180L, groups = LocationRequestGroup.class)
    private Double lng;

    private String fullName;

    @NotEmpty(message = "{com.constraint.FieldEmpty.message}", groups = RegisterRequestGroup.class)
    private String username;

    @NotEmpty(message = "{com.constraint.FieldEmpty.message}", groups = RegisterRequestGroup.class)
    private String password;

    @NotEmpty(message = "{com.constraint.FieldEmpty.message}")
    @Pattern(regexp = "(^[0-9]{4})", message = "{com.constraint.OtpValidation.message}")
    private String mobileOtp;

    @NotEmpty(message = "{com.constraint.FieldEmpty.message}", groups = LocationRequestGroup.class)
    private String provider;

    private String nationality;
    private String city;

    @NotEmpty(message = "{com.constraint.FieldEmpty.message}")
    private List<String> addons;

    public interface CustomProfileRequestGroup {

    }

    public interface ContactInfoRequestGroup {

    }

    public interface LocationRequestGroup {

    }

    public interface RegisterRequestGroup {

    }
}

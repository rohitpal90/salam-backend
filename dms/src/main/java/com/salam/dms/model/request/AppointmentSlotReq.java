package com.salam.dms.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AppointmentSlotReq {
    @NotEmpty(message = "appointmentId is mandatory")
    private String appointmentId;
    @NotEmpty(message = "day is mandatory")
    private String day;
}

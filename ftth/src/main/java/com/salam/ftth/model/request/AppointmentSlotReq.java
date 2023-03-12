package com.salam.ftth.model.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AppointmentSlotReq {
    @NotEmpty(message = "{com.constraint.FieldEmpty.message}")
    private String appointmentId;
    @NotEmpty(message = "{com.constraint.FieldEmpty.message}")
    private String day;
}

package com.salam.dms.model.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AppointmentBookRequest {
    @NotEmpty(message = "{com.constraint.FieldEmpty.message}")
    private String slotId;
}

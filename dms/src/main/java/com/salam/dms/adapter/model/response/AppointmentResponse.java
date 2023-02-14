package com.salam.dms.adapter.model.response;

import com.salam.dms.adapter.model.Appointment;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class AppointmentResponse {
    public String status;
    public List<Appointment> appointmentList = Collections.emptyList();
}

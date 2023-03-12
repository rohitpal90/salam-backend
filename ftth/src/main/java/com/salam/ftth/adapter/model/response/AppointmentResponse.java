package com.salam.ftth.adapter.model.response;

import com.salam.ftth.adapter.model.Appointment;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class AppointmentResponse {
    public String status;
    public List<Appointment> appointmentList = Collections.emptyList();
}

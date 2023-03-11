package com.salam.ftth.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.salam.ftth.adapter.feign.client.AppointmentClient;
import com.salam.ftth.adapter.feign.mock.ClientMockAdapter;
import com.salam.ftth.adapter.model.Appointment;
import com.salam.ftth.adapter.model.request.AppointmentRequest;
import com.salam.ftth.adapter.model.response.AppointmentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class AppointmentService {

    private static final SimpleDateFormat legacyFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat currentFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static final Comparator<Appointment> APPOINTMENT_DT_COMP =
            Comparator.comparing(Appointment::getAppointmentDatetime);

    @Autowired
    AppointmentClient appointmentClient;

    @Autowired
    ClientMockAdapter clientMockAdapter;


    public List<Appointment> fetchAppointments(AppointmentRequest request) {
        // AppointmentResponse response = appointmentClient.fetchAppointmentSlots(request);
        AppointmentResponse response = clientMockAdapter.getFor("appointments", new TypeReference<>() { });
        return response.getAppointmentList();
    }

    public List<Appointment> findAvailableAppointments(String date, String time, AppointmentRequest request)
            throws ParseException {
        // fetch available appointments
        var appointments = fetchAppointments(request);

        // parse logic
        String formattedDate = legacyFormat.format(currentFormat.parse(date));
        String reqDateTime = String.join(",", formattedDate, time);

        var reqAppointment = new Appointment();
        reqAppointment.setAppointmentDatetime(reqDateTime);
        return this.findAvailableAppointments(reqAppointment, appointments);
    }

    private List<Appointment> findAvailableAppointments(Appointment reqAppointment, List<Appointment> appointments) {
        return appointments.stream()
                .filter(appointment -> APPOINTMENT_DT_COMP.compare(appointment, reqAppointment) == 0)
                .toList();
    }

}

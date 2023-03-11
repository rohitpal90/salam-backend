package com.salam.ftth.controllers;

import com.salam.ftth.adapter.model.Appointment;
import com.salam.ftth.adapter.model.request.AppointmentRequest;
import com.salam.ftth.model.Event;
import com.salam.ftth.model.RequestContext;
import com.salam.ftth.model.request.AppointmentBookRequest;
import com.salam.ftth.model.response.EventResult;
import com.salam.ftth.services.AppointmentService;
import com.salam.ftth.services.StateMachineService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final StateMachineService stateMachineService;


    @GetMapping
    public List<Appointment> fetchAppointments() {
        var appointmentRequest = new AppointmentRequest();
        appointmentRequest.setAccessMode("GPON");
        appointmentRequest.setStartDate("2022-11-11");
        appointmentRequest.setSubsPlanId("4003");
        return appointmentService.fetchAppointments(appointmentRequest);
    }

    @PostMapping("/book")
    public EventResult bookAppointment(@RequestBody @Valid AppointmentBookRequest request,
                                       @RequestParam("reqId") RequestContext requestContext) {
        requestContext.setAppointmentBookRequest(request);
        return stateMachineService.trigger(Event.SCHEDULE, requestContext);
    }

}

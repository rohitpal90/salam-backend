package com.salam.dms.controllers;

import com.salam.dms.adapter.model.Appointment;
import com.salam.dms.adapter.model.request.AppointmentRequest;
import com.salam.dms.config.sm.StateMachineAdapter;
import com.salam.dms.model.Event;
import com.salam.dms.model.RequestContext;
import com.salam.dms.model.request.AppointmentBookRequest;
import com.salam.dms.services.AppointmentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final StateMachineAdapter stateMachineAdapter;


    @GetMapping
    public List<Appointment> fetchAppointments() {
        var appointmentRequest = new AppointmentRequest();
        appointmentRequest.setAccessMode("GPON");
        appointmentRequest.setStartDate("2022-11-11");
        appointmentRequest.setSubsPlanId("4003");
        return appointmentService.fetchAppointments(appointmentRequest);
    }

    @PostMapping("/book")
    public Object bookAppointment(@RequestBody AppointmentBookRequest request,
                                  @RequestParam("reqId") RequestContext requestContext) {
        requestContext.setAppointmentBookRequest(request);
        return stateMachineAdapter.trigger(Event.SCHEDULE, requestContext).block();
    }

}

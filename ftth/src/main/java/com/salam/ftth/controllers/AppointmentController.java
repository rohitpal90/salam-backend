package com.salam.ftth.controllers;

import com.salam.ftth.adapter.model.Appointment;
import com.salam.ftth.adapter.model.request.AppointmentRequest;
import com.salam.ftth.services.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/appointments")
@Tag(name = "Appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;


    @GetMapping
    @Operation(
            summary = "Get appointments",
            responses = {
                    @ApiResponse(responseCode = "200", ref = "AppointmentResponse"),
                    @ApiResponse(responseCode = "400", ref = "InvalidStateResponse"),
            }
    )
    public List<Appointment> fetchAppointments() {
        var appointmentRequest = new AppointmentRequest();
        appointmentRequest.setAccessMode("GPON");
        appointmentRequest.setStartDate("2022-11-11");
        appointmentRequest.setSubsPlanId("4003");
        return appointmentService.fetchAppointments(appointmentRequest);
    }
}

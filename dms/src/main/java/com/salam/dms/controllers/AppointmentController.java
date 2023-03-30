package com.salam.dms.controllers;

import com.salam.dms.adapter.model.Appointment;
import com.salam.dms.adapter.model.request.AppointmentRequest;
import com.salam.dms.config.sm.StateMachineAdapter;
import com.salam.dms.model.Event;
import com.salam.dms.model.EventResult;
import com.salam.dms.model.RequestContext;
import com.salam.dms.model.request.AppointmentBookRequest;
import com.salam.dms.services.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/appointments")
@Tag(name = "Appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final StateMachineAdapter stateMachineAdapter;


    @GetMapping
    @Operation(
            summary = "Get appointments",
            responses = {
                    @ApiResponse(responseCode = "200", ref = "AppointmentResponse"),
                    @ApiResponse(responseCode = "400", ref = "InvalidStateResponse"),
                    @ApiResponse(responseCode = "401", ref = "UnauthenticatedResponse"),
            }
    )
    public List<Appointment> fetchAppointments() {
        var appointmentRequest = new AppointmentRequest();
        appointmentRequest.setAccessMode("GPON");
        appointmentRequest.setStartDate("2022-11-11");
        appointmentRequest.setSubsPlanId("4003");
        return appointmentService.fetchAppointments(appointmentRequest);
    }

    @PostMapping("/book")
    @Operation(
            summary = "Book installation appointment",
            responses = {
                    @ApiResponse(responseCode = "200", ref = "AppointmentBookResponse"),
                    @ApiResponse(responseCode = "400", ref = "InvalidStateResponse"),
                    @ApiResponse(responseCode = "401", ref = "UnauthenticatedResponse"),
                    @ApiResponse(responseCode = "404", ref = "NotFoundResponse")
            }
    )
    public EventResult bookAppointment(@RequestBody @Valid AppointmentBookRequest request,
                                       @RequestParam("reqId") RequestContext requestContext) {
        requestContext.setAppointmentBookRequest(request);
        return stateMachineAdapter.trigger(Event.SCHEDULE, requestContext).block();
    }

}

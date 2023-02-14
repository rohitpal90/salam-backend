package com.salam.dms.services.guards;

import com.salam.dms.adapter.model.request.AppointmentRequest;
import com.salam.dms.config.sm.GuardHandler;
import com.salam.dms.model.Event;
import com.salam.dms.model.RequestContext;
import com.salam.dms.model.States;
import com.salam.dms.services.AppointmentService;
import lombok.AllArgsConstructor;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AppointmentBookGuard implements GuardHandler {

    private static AppointmentService appointmentService;


    @Override
    public boolean handle(StateContext<States, Event> context) {
        var requestContext = RequestContext.fromStateContext(context);

        // use slot
        var request = requestContext.getAppointmentBookRequest();

        // temp appointment request
        var appointmentRequest = new AppointmentRequest();
        appointmentRequest.setAccessMode("GPON");
        appointmentRequest.setStartDate("2022-11-11");
        appointmentRequest.setSubsPlanId("4003");
        var appointments = appointmentService.fetchAppointments(appointmentRequest);

        return appointments.size() > 0;
    }

    @Override
    public Event forType() {
        return Event.SCHEDULE;
    }
}

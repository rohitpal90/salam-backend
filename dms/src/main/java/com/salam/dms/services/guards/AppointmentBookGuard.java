package com.salam.dms.services.guards;

import com.salam.dms.adapter.model.request.AppointmentRequest;
import com.salam.dms.config.exception.AppError;
import com.salam.dms.config.exception.AppErrors;
import com.salam.dms.config.sm.GuardHandler;
import com.salam.dms.model.Event;
import com.salam.dms.model.RequestContext;
import com.salam.dms.model.States;
import com.salam.dms.services.AppointmentService;
import lombok.AllArgsConstructor;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import static com.salam.dms.config.exception.AppErrors.NO_APPOINTMENTS_FOUND;

@Component
@AllArgsConstructor
public class AppointmentBookGuard extends GuardHandler {

    private final AppointmentService appointmentService;


    @Override
    public void handle(StateContext<States, Event> context) {
        var requestContext = RequestContext.fromStateContext(context);

        // use slot
        var request = requestContext.getAppointmentBookRequest();

        // temp appointment request
        var appointmentRequest = new AppointmentRequest();
        appointmentRequest.setAccessMode("GPON");
        appointmentRequest.setStartDate("2022-11-11");
        appointmentRequest.setSubsPlanId("4003");
        var appointments = appointmentService.fetchAppointments(appointmentRequest);

        if (CollectionUtils.isEmpty(appointments)) {
            throw AppError.create(NO_APPOINTMENTS_FOUND);
        }

        var metaInfo = requestContext.getMetaInfo();
        metaInfo.setAppointment(appointments.get(0));
        requestContext.setMetaInfo(metaInfo);
    }

    @Override
    public Event forType() {
        return Event.SCHEDULE;
    }

}

package com.salam.ftth.services.guards;

import com.salam.ftth.adapter.model.request.AppointmentRequest;
import com.salam.ftth.config.exception.AppError;
import com.salam.ftth.model.Event;
import com.salam.ftth.model.RequestContext;
import com.salam.ftth.services.AppointmentService;
import com.salam.libs.sm.config.GuardHandler;
import lombok.AllArgsConstructor;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import static com.salam.ftth.config.exception.AppErrors.NO_APPOINTMENTS_FOUND;

@Component
@AllArgsConstructor
public class AppointmentBookGuard extends GuardHandler {

    private final AppointmentService appointmentService;


    @Override
    public void handle(StateContext<String, String> context) {
        var stateMachine = context.getStateMachine();
        var requestContext = RequestContext.<RequestContext>fromStateMachine(stateMachine);

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

        var metaInfo = requestContext.getMeta();
        metaInfo.setAppointment(appointments.get(0));
        requestContext.setMeta(metaInfo);
    }

    @Override
    public String forType() {
        return Event.SCHEDULE.name();
    }

}

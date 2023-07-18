package com.salam.ftth.model;

import com.salam.ftth.model.request.AppointmentBookRequest;
import eu.fraho.spring.securityJwt.base.dto.JwtUser;

public class RequestContext extends com.salam.libs.sm.model.RequestContext<RequestMetaInfo> {

    private JwtUser actor;

    // requests
    private AppointmentBookRequest appointmentBookRequest;

    public static final String KEY = "requestContext";
    public static final String ERROR_KEY = "error";

    public RequestContext(String orderId, Long userId) {
        super(orderId, userId);
    }

    public void setVerified(boolean verified) {
        getMeta().setVerified(verified);
    }

    public void setPlanInfo(PlanInfo planInfo) {
        this.getMeta().setPlanInfo(planInfo);
    }

    public JwtUser getActor() {
        return actor;
    }

    public void setActor(JwtUser actor) {
        this.actor = actor;
    }

    public AppointmentBookRequest getAppointmentBookRequest() {
        return appointmentBookRequest;
    }

    public void setAppointmentBookRequest(AppointmentBookRequest appointmentBookRequest) {
        this.appointmentBookRequest = appointmentBookRequest;
    }

    @Override
    public Class<RequestMetaInfo> getMetaClass() {
        return RequestMetaInfo.class;
    }
}


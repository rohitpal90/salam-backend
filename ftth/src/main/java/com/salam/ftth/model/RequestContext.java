package com.salam.ftth.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.salam.ftth.model.request.AppointmentBookRequest;
import com.salam.ftth.model.request.VerifyCustomerRequest;
import eu.fraho.spring.securityJwt.base.dto.JwtUser;

public class RequestContext extends com.salam.libs.sm.model.RequestContext<RequestMetaInfo> {

    private JwtUser actor;

    // requests
    private VerifyCustomerRequest verifyInfo;
    private AppointmentBookRequest appointmentBookRequest;

    public static final String KEY = "requestContext";
    public static final String ERROR_KEY = "error";

    public RequestContext(String orderId, Long userId) {
        super(orderId, userId);
    }

    public void setVerifySmsRequest(VerifyCustomerRequest verifyInfo) {
        this.verifyInfo = verifyInfo;
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

    public VerifyCustomerRequest getVerifyInfo() {
        return verifyInfo;
    }

    public void setVerifyInfo(VerifyCustomerRequest verifyInfo) {
        this.verifyInfo = verifyInfo;
    }

    @Override
    public Class<RequestMetaInfo> getMetaClass() {
        return RequestMetaInfo.class;
    }
}


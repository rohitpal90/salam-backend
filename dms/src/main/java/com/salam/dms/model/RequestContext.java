package com.salam.dms.model;

import com.salam.dms.db.entity.Request;
import com.salam.dms.model.request.AppointmentBookRequest;
import com.salam.dms.model.request.CustomerProfileRequest;
import com.salam.dms.model.request.VerifyCustomerRequest;
import eu.fraho.spring.securityJwt.base.dto.JwtUser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;

@Data
@EqualsAndHashCode
public class RequestContext {
    private Long requestId;
    private RequestMetaInfo metaInfo;
    private JwtUser actor;
    private VerifyCustomerRequest verifyCustomerRequest;
    private AppointmentBookRequest appointmentBookRequest;

    public static final String KEY = "requestContext";

    @SneakyThrows
    public RequestContext(Long requestId, RequestMetaInfo metaInfo) {
        this.requestId = requestId;
        this.metaInfo = metaInfo;
    }

    public static RequestContext fromRequest(Request request) {
        return new RequestContext(request.getId(), request.getMeta());
    }

    public static RequestContext fromStateMachine(StateMachine<States, Event> stateMachine) {
        return ((RequestContext) stateMachine.getExtendedState().getVariables().get(KEY));
    }

    public static RequestContext createNew(CustomerProfileRequest profileRequest) {
        return new RequestContext(null, new RequestMetaInfo(profileRequest));
    }

    public static RequestContext fromStateContext(StateContext<States, Event> stateContext) {
        var stateMachine = stateContext.getStateMachine();
        return RequestContext.fromStateMachine(stateMachine);
    }

    public void setVerifySmsRequest(VerifyCustomerRequest verifyCustomerRequest) {
        this.verifyCustomerRequest = verifyCustomerRequest;
    }

    public void setVerified(boolean verified) {
        metaInfo.setVerified(verified);
    }

    public void setToStateMachineState(StateMachine<States, Event> stateMachine) {
        stateMachine.getExtendedState().getVariables().put(KEY, this);
    }

    public void setPlanInfo(PlanInfo planInfo) {
        this.getMetaInfo().setPlanInfo(planInfo);
    }

}


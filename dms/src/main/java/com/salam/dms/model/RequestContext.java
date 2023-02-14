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
    // states
    private Long requestId;
    private String orderId;
    private RequestMetaInfo metaInfo;
    private JwtUser actor;
    private Exception currentError;

    // requests
    private VerifyCustomerRequest verifyCustomerRequest;
    private AppointmentBookRequest appointmentBookRequest;

    public static final String KEY = "requestContext";
    public static final String ERROR_KEY = "error";

    @SneakyThrows
    public RequestContext(Request request) {
        this.requestId = request.getId();
        this.orderId = request.getOrderId();
        this.metaInfo = request.getMeta();
    }

    public static RequestContext fromRequest(Request request) {
        return new RequestContext(request);
    }

    public static RequestContext fromStateMachine(StateMachine<States, Event> stateMachine) {
        var variables = stateMachine.getExtendedState().getVariables();
        var reqContext = ((RequestContext) variables.get(KEY));

        if (variables.containsKey(ERROR_KEY)) {
            reqContext.setCurrentError((Exception) variables.get(ERROR_KEY));
        }

        return reqContext;
    }

    public static RequestContext createNew(CustomerProfileRequest profileRequest) {
        var request = new Request();
        request.setMeta(new RequestMetaInfo(profileRequest));
        return new RequestContext(request);
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


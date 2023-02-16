package com.salam.libs.sm.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.salam.libs.sm.entity.Request;
import org.springframework.statemachine.StateMachine;

public class RequestContext {
    private Long requestId;
    private String orderId;
    private JsonNode meta;
    private Exception currentError;

    public static final String KEY = "requestContext";
    public static final String ERROR_KEY = "error";

    public RequestContext(Request request) {
        this.requestId = request.getId();
        this.orderId = request.getOrderId();
        this.meta = request.getMeta();
    }


    public static RequestContext fromRequest(Request request) {
        return new RequestContext(request);
    }

    public static RequestContext fromStateMachine(StateMachine<String, String> stateMachine) {
        var variables = stateMachine.getExtendedState().getVariables();
        var reqContext = ((RequestContext) variables.get(KEY));

        if (variables.containsKey(ERROR_KEY)) {
            reqContext.setCurrentError((Exception) variables.get(ERROR_KEY));
        }

        return reqContext;
    }

    public void setToStateMachineState(StateMachine<String, String> stateMachine) {
        stateMachine.getExtendedState().getVariables().put(KEY, this);
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public JsonNode getMeta() {
        return meta;
    }

    public void setMeta(JsonNode meta) {
        this.meta = meta;
    }

    public Exception getCurrentError() {
        return currentError;
    }

    public void setCurrentError(Exception currentError) {
        this.currentError = currentError;
    }
}

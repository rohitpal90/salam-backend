package com.salam.libs.sm.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.statemachine.StateMachine;

public abstract class RequestContext {
    private Long requestId;
    private String orderId;
    private Exception currentError;
    private static final ObjectMapper mapper = new ObjectMapper();

    public static final String KEY = "requestContext";
    public static final String ERROR_KEY = "error";

    public RequestContext(String orderId) {
        this.orderId = orderId;
    }

    public static <T extends RequestContext> T fromStateMachine(StateMachine<String, String> stateMachine) {
        var variables = stateMachine.getExtendedState().getVariables();
        var reqContext = variables.get(KEY);

        if (variables.containsKey(ERROR_KEY)) {
            ((RequestContext) reqContext).setCurrentError((Exception) variables.get(ERROR_KEY));
        }

        return ((T) reqContext);
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

    public Exception getCurrentError() {
        return currentError;
    }

    public void setCurrentError(Exception currentError) {
        this.currentError = currentError;
    }

    public ObjectMapper getMapper() {
        return mapper;
    }

    public abstract JsonNode getMeta();
}

package com.salam.dms.model;

import org.springframework.statemachine.StateMachineEventResult;

public class EventResult {

    private final StateMachineEventResult<States, Event> result;
    private RequestContext requestContext;

    public EventResult(StateMachineEventResult<States, Event> result) {
        this.result = result;
    }

    public EventResult(StateMachineEventResult<States, Event> result, RequestContext requestContext) {
        this.result = result;
        this.requestContext = requestContext;
    }

    public StateMachineEventResult.ResultType getResultType() {
        return result.getResultType();
    }

    public Event getEvent() {
        return result.getMessage().getPayload();
    }

    public Long getRequestId() {
        return requestContext.getRequestId();
    }

}

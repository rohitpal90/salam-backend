package com.salam.libs.sm.model;

import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineEventResult;

import java.util.List;

import static org.springframework.statemachine.StateMachineEventResult.ResultType.DEFERRED;
import static org.springframework.statemachine.StateMachineEventResult.ResultType.DENIED;

public class EventResult {

    private final RequestContext requestContext;
    private final StateMachineEventResult<String, String> result;
    private final StateMachine<String, String> sm;


    public EventResult(StateMachineEventResult<String, String> result, StateMachine<String, String> sm) {
        this.sm = sm;
        this.requestContext = RequestContext.fromStateMachine(sm);
        this.result = result;
    }

    public boolean isDeniedOrDeferred() {
        var resultType = result.getResultType();
        return List.of(DEFERRED, DENIED).contains(resultType);
    }

    public RequestContext getRequestContext() {
        return requestContext;
    }

    public StateMachineEventResult<String, String> getResult() {
        return result;
    }

    public boolean hasStateMachineError() {
        return this.sm.hasStateMachineError();
    }

    public StateMachine<String, String> getSm() {
        return sm;
    }
}

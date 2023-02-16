package com.salam.libs.sm.model;

import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineEventResult;

import java.util.List;

import static org.springframework.statemachine.StateMachineEventResult.ResultType.DEFERRED;
import static org.springframework.statemachine.StateMachineEventResult.ResultType.DENIED;

public class EventResult {

    private final RequestContext requestContext;
    private final StateMachineEventResult<String, String> result;


    public EventResult(StateMachineEventResult<String, String> result, StateMachine<String, String> sm) {
        this.requestContext = RequestContext.fromStateMachine(sm);
        this.result = result;
    }

    public boolean isDeniedOrDeferred(StateMachineEventResult<String, String> result) {
        var resultType = result.getResultType();
        return List.of(DEFERRED, DENIED).contains(resultType);
    }

}

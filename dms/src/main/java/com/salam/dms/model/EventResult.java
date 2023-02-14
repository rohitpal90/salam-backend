package com.salam.dms.model;

import com.salam.dms.config.exception.AppError;
import com.salam.dms.config.exception.AppErrors;
import com.salam.dms.model.response.ResponseStatus;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineEventResult;

import java.util.List;

import static com.salam.dms.config.exception.AppErrors.DMS_APP_ERROR;
import static com.salam.dms.config.exception.AppErrors.INVALID_STATE;
import static com.salam.dms.model.response.ResponseStatus.FAILURE;
import static com.salam.dms.model.response.ResponseStatus.SUCCESS;
import static org.springframework.statemachine.StateMachineEventResult.ResultType.DEFERRED;
import static org.springframework.statemachine.StateMachineEventResult.ResultType.DENIED;

public class EventResult {

    private final RequestContext requestContext;


    public EventResult(StateMachineEventResult<States, Event> result, StateMachine<States, Event> sm) {
        this.requestContext = RequestContext.fromStateMachine(sm);

        if (hasAppError()) {
            throw (AppError) requestContext.getCurrentError();
        }

        if (isDeniedOrDeferred(result)) {
            throw AppError.create(INVALID_STATE);
        }

        if (sm.hasStateMachineError()) {
            throw AppError.create(FAILURE.getKey(), DMS_APP_ERROR);
        }
    }

    public boolean isDeniedOrDeferred(StateMachineEventResult<States, Event> result) {
        var resultType = result.getResultType();
        return List.of(DEFERRED, DENIED).contains(resultType);
    }

    public boolean hasAppError() {
        var error = requestContext.getCurrentError();
        return error instanceof AppError;
    }

    public ResponseStatus getMessage() {
        return SUCCESS;
    }

    public String getReqId() {
        return requestContext.getOrderId();
    }

}

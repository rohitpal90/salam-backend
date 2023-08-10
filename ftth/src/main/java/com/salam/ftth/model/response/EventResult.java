package com.salam.ftth.model.response;

import com.salam.ftth.config.exception.AppError;
import com.salam.ftth.model.RequestContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineEventResult;

import java.util.List;

import static com.salam.ftth.config.exception.AppErrors.FTTH_APP_ERROR;
import static com.salam.ftth.config.exception.AppErrors.INVALID_STATE;
import static com.salam.ftth.model.response.ResponseStatus.FAILURE;
import static com.salam.ftth.model.response.ResponseStatus.SUCCESS;
import static org.springframework.statemachine.StateMachineEventResult.ResultType.DEFERRED;
import static org.springframework.statemachine.StateMachineEventResult.ResultType.DENIED;

public class EventResult {

    private final RequestContext requestContext;
    private final StateMachine<String, String> sm;


    public EventResult(com.salam.libs.sm.model.EventResult eventResult) {
        this.sm = eventResult.getSm();
        this.requestContext = RequestContext.fromStateMachine(sm);

        if (hasAppError()) {
            throw (AppError) requestContext.getCurrentError();
        }

        var result = eventResult.getResult();
        if (isDeniedOrDeferred(result)) {
            throw AppError.create(INVALID_STATE);
        }

        if (sm.hasStateMachineError()) {
            throw AppError.create(FAILURE.getKey(), FTTH_APP_ERROR);
        }
    }

    public boolean isDeniedOrDeferred(StateMachineEventResult<String, String> result) {
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

    public String getState() {
        return sm.getState().getId();
    }
}

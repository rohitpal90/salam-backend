package com.salam.ftth.model.response;

import com.salam.ftth.config.exception.AppError;
import com.salam.libs.sm.model.RequestContext;

import static com.salam.ftth.config.exception.AppErrors.FTTH_APP_ERROR;
import static com.salam.ftth.config.exception.AppErrors.INVALID_STATE;
import static com.salam.ftth.model.response.ResponseStatus.FAILURE;
import static com.salam.ftth.model.response.ResponseStatus.SUCCESS;

public class EventResult {

    private final RequestContext requestContext;


    public EventResult(com.salam.libs.sm.model.EventResult result) {
        this.requestContext = result.getRequestContext();

        if (hasAppError()) {
            throw (AppError) requestContext.getCurrentError();
        }

        if (result.isDeniedOrDeferred()) {
            throw AppError.create(INVALID_STATE);
        }

        if (result.hasStateMachineError()) {
            throw AppError.create(FAILURE.getKey(), FTTH_APP_ERROR);
        }
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

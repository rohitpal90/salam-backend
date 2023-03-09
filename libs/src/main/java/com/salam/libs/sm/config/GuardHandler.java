package com.salam.libs.sm.config;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

public abstract class GuardHandler implements Guard<String, String> {

    @Override
    public boolean evaluate(StateContext<String, String> context) {
        try {
            handle(context);
            return true;
        } catch (Exception e) {
            setContextError(context, e);
            throw e;
        }
    }

    private void setContextError(StateContext<String, String> context, Exception error) {
        context.getStateMachine().setStateMachineError(error);
        context.getExtendedState().getVariables().put("error", error);
    }

    public abstract void handle(StateContext<String, String> context);

    public abstract String forType();

}

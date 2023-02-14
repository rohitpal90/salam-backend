package com.salam.dms.config.sm;

import com.salam.dms.model.Event;
import com.salam.dms.model.States;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

public abstract class GuardHandler implements Guard<States, Event> {

    @Override
    public boolean evaluate(StateContext<States, Event> context) {
        try {
            handle(context);
            return true;
        } catch (Exception e) {
            setContextError(context, e);
            throw e;
        }
    }

    private void setContextError(StateContext<States, Event> context, Exception error) {
        context.getStateMachine().setStateMachineError(error);
        context.getExtendedState().getVariables().put("error", error);
    }

    public abstract void handle(StateContext<States, Event> context);

    public abstract Event forType();

}

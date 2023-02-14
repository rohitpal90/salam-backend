package com.salam.dms.config.sm;

import com.salam.dms.model.Event;
import com.salam.dms.model.States;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

public interface GuardHandler extends Guard<States, Event> {

    @Override
    public default boolean evaluate(StateContext<States, Event> context) {
        return handle(context);
    }

    boolean handle(StateContext<States, Event> context);
    Event forType();

}

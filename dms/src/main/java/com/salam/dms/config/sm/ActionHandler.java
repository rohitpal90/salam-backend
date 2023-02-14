package com.salam.dms.config.sm;

import com.salam.dms.model.Event;
import com.salam.dms.model.States;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

public interface ActionHandler extends Action<States, Event> {

    @Override
    public default void execute(StateContext<States, Event> context) {
        handle(context);
    }
    void handle(StateContext<States, Event> context);
    Event forType();
}

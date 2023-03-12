package com.salam.libs.sm.config;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

public interface ActionHandler extends Action<String, String> {

    @Override
    default void execute(StateContext<String, String> context) {
        handle(context);
    }
    void handle(StateContext<String, String> context);
    String forType();
}

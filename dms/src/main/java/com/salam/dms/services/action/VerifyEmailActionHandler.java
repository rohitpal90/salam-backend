package com.salam.dms.services.action;

import com.salam.dms.config.sm.ActionHandler;
import com.salam.dms.model.Event;
import com.salam.dms.model.States;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

@Component
public class VerifyEmailActionHandler implements ActionHandler {


    @Override
    public void handle(StateContext<States, Event> context) {

    }

    @Override
    public Event forType() {
        return Event.VERIFY_EMAIL;
    }

}

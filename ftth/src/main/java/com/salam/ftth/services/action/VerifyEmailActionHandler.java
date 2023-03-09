package com.salam.ftth.services.action;

import com.salam.ftth.model.Event;
import com.salam.libs.sm.config.ActionHandler;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

@Component
public class VerifyEmailActionHandler implements ActionHandler {


    @Override
    public void handle(StateContext<String, String> context) {

    }

    @Override
    public String forType() {
        return Event.VERIFY_EMAIL.name();
    }

}

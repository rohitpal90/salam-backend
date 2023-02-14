package com.salam.dms.services.guards;

import com.salam.dms.config.sm.GuardHandler;
import com.salam.dms.model.Event;
import com.salam.dms.model.RequestContext;
import com.salam.dms.model.States;
import com.salam.dms.services.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class VerifySmsGuard implements GuardHandler {

    @Autowired
    CustomerService customerService;

    @Override
    public boolean handle(StateContext<States, Event> context) {
        var stateMachine = context.getStateMachine();
        var requestContext = RequestContext.fromStateMachine(stateMachine);
        var verifyCustomerRequest = requestContext.getVerifyCustomerRequest();

        String mobileOtp = verifyCustomerRequest.getMobileOtp();
        if (customerService.verifyBySms(mobileOtp, requestContext)) {
            requestContext.setVerified(true);
            requestContext.setToStateMachineState(stateMachine);
            return true;
        }

        return false;
    }

    @Override
    public Event forType() {
        return Event.VERIFY_MOBILE;
    }

}

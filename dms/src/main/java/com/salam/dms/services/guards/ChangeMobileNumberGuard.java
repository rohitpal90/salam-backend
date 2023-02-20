package com.salam.dms.services.guards;

import com.salam.dms.config.sm.GuardHandler;
import com.salam.dms.model.Event;
import com.salam.dms.model.RequestContext;
import com.salam.dms.model.RequestMetaInfo;
import com.salam.dms.model.States;
import com.salam.dms.services.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ChangeMobileNumberGuard extends GuardHandler {

    private final CustomerService customerService;


    @Override
    public void handle(StateContext<States, Event> context) {
        var stateMachine = context.getStateMachine();
        var requestContext = RequestContext.fromStateMachine(stateMachine);

        RequestMetaInfo metaInfo = requestContext.getMetaInfo();
        var customerInfo = metaInfo.getCustomerInfo();

        // update phone
        var changeCustomerPhoneRequest = requestContext.getChangeCustomerPhoneRequest();
        var newPhone = changeCustomerPhoneRequest.getMobile();
        customerInfo.setMobile(newPhone);

        // resend verification request
        var verifyResponse = customerService.createPhoneVerifyRequest(newPhone);
        metaInfo.setVerifyBySmsResponse(verifyResponse);
    }

    @Override
    public Event forType() {
        return Event.CHANGE_MOBILE;
    }
}

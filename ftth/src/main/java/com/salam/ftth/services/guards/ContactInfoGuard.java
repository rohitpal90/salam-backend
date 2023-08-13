package com.salam.ftth.services.guards;

import com.salam.ftth.model.Event;
import com.salam.ftth.model.RequestContext;
import com.salam.libs.sm.config.GuardHandler;
import lombok.AllArgsConstructor;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ContactInfoGuard extends GuardHandler {

    @Override
    public void handle(StateContext<String, String> context) {
        var stateMachine = context.getStateMachine();
        var requestContext = RequestContext.<RequestContext>fromStateMachine(stateMachine);

        var contactInfoRequest = requestContext.getCustomerProfileRequest();
        var metaInfo = requestContext.getMeta();

        var customerInfo = metaInfo.getCustomerInfo();
        customerInfo.setEmail(contactInfoRequest.getEmail());
        customerInfo.setMobile(contactInfoRequest.getMobile());

        metaInfo.setCustomerInfo(customerInfo);
    }

    @Override
    public String forType() {
        return Event.ADD_CONTACT.name();
    }
}

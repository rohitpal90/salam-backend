package com.salam.dms.services.guards;

import com.salam.dms.config.sm.GuardHandler;
import com.salam.dms.model.Event;
import com.salam.dms.model.RequestContext;
import com.salam.dms.model.States;
import lombok.AllArgsConstructor;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CustomerConfirmGuard extends GuardHandler {

    @Override
    public void handle(StateContext<States, Event> context) {
        var sm = context.getStateMachine();
        var requestContext = RequestContext.fromStateMachine(sm);

        var paymentInfoRequest = requestContext.getPaymentInfoRequest();
        var metaInfo = requestContext.getMetaInfo();

        metaInfo.setPaymentInfo(paymentInfoRequest);
    }

    @Override
    public Event forType() {
        return Event.CUSTOMER_CONFIRM;
    }
}

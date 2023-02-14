package com.salam.dms.services.guards;

import com.salam.dms.config.sm.GuardHandler;
import com.salam.dms.model.Event;
import com.salam.dms.model.RequestContext;
import com.salam.dms.model.States;
import com.salam.dms.services.CustomerService;
import com.salam.dms.services.PlanService;
import lombok.AllArgsConstructor;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@AllArgsConstructor
public class CustomerProfileGuard implements GuardHandler {

    private final PlanService planService;
    private final CustomerService customerService;

    @Override
    public boolean handle(StateContext<States, Event> context) {
        var stateMachine = context.getStateMachine();
        var requestContext = RequestContext.fromStateMachine(stateMachine);
        var metaInfo = requestContext.getMetaInfo();

        // validate dealer plan
        var dealerId = requestContext.getActor().getId();
        var planInfo = metaInfo.getPlanInfo();
        var planId = planInfo.getPlanId();
        planService.checkDealerPlan(planId, dealerId);

        // send verify request
        var customerInfo = metaInfo.getCustomerInfo();
        var verifyResponse = customerService.createPhoneVerifyRequest(customerInfo.getMobile());
        metaInfo.setVerifyBySmsResponse(verifyResponse);

        return Objects.nonNull(requestContext.getMetaInfo().getCustomerInfo());
    }

    @Override
    public Event forType() {
        return Event.CREATE_ACCOUNT;
    }

}

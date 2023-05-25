package com.salam.dms.services.guards;

import com.salam.dms.config.sm.GuardHandler;
import com.salam.dms.model.Event;
import com.salam.dms.model.RequestContext;
import com.salam.dms.model.States;
import com.salam.dms.model.IdentityInfo;
import com.salam.dms.services.CustomerService;
import com.salam.dms.services.PlanService;
import lombok.AllArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CustomerProfileGuard extends GuardHandler {

    private final PlanService planService;
    private final CustomerService customerService;

    @Override
    public void handle(StateContext<States, Event> context) {
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
        metaInfo.setCustomerInfo(requestContext.getCustomerProfileRequest());

        var locale = LocaleContextHolder.getLocale();
        var ninVerifyResponse = customerService.createNinVerifyRequest(customerInfo);
        var customerAddresses = customerService.getCustomerAddresses(customerInfo, locale);
        var identityInfo = new IdentityInfo(ninVerifyResponse, customerAddresses);
        metaInfo.setIdentityInfo(identityInfo);

        var plan = planService.getPlanDetail(planId);
        var verifyResponse = customerService.createPhoneVerifyRequest(customerInfo, plan, locale);
        metaInfo.setVerifyInfo(verifyResponse);
    }

    @Override
    public Event forType() {
        return Event.CREATE_ACCOUNT;
    }

}

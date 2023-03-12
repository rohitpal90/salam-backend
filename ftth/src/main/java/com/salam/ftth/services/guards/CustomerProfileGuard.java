package com.salam.ftth.services.guards;

import com.salam.ftth.model.Event;
import com.salam.ftth.model.RequestContext;
import com.salam.ftth.services.CustomerService;
import com.salam.ftth.services.PlanService;
import com.salam.libs.sm.config.GuardHandler;
import lombok.AllArgsConstructor;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CustomerProfileGuard extends GuardHandler {

    private final PlanService planService;
    private final CustomerService customerService;

    @Override
    public void handle(StateContext<String, String> context) {
        var stateMachine = context.getStateMachine();
        var requestContext = RequestContext.<RequestContext>fromStateMachine(stateMachine);
        var metaInfo = requestContext.getMeta();

        // validate dealer plan
        var planInfo = metaInfo.getPlanInfo();
        var planId = planInfo.getPlanId();
        planService.checkPlan(planId);

        // send verify request
        var customerInfo = metaInfo.getCustomerInfo();
        var verifyResponse = customerService.createPhoneVerifyRequest(customerInfo.getMobile());
        metaInfo.setVerifyInfo(verifyResponse);
    }

    @Override
    public String forType() {
        return Event.CREATE_ACCOUNT.name();
    }

}

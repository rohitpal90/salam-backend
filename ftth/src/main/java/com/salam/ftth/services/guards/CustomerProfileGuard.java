package com.salam.ftth.services.guards;

import com.salam.ftth.config.exception.AppError;
import com.salam.ftth.config.exception.AppErrors;
import com.salam.ftth.model.Event;
import com.salam.ftth.model.IdentityInfo;
import com.salam.ftth.model.RequestContext;
import com.salam.ftth.services.CustomerService;
import com.salam.ftth.services.PlanService;
import com.salam.libs.feign.elm.model.SendOtpResponse;
import com.salam.libs.sm.config.GuardHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@AllArgsConstructor
public class CustomerProfileGuard extends GuardHandler {

    private final PlanService planService;
    private final CustomerService customerService;

    @Override
    public void handle(StateContext<String, String> context) {
        var stateMachine = context.getStateMachine();
        var requestContext = RequestContext.<RequestContext>fromStateMachine(stateMachine);

        var profileRequest = requestContext.getCustomerProfileRequest();
        var metaInfo = requestContext.getMeta();
        var customerInfo = metaInfo.getCustomerInfo();

        var planInfo = metaInfo.getPlanInfo();
        planService.checkPlan(planInfo.getPlanId());

        var locale = LocaleContextHolder.getLocale();
//        var identityInfo = customerService.verifyAndGetCustomerInfo(customerInfo);

        if (customerInfo.getId().equals("2223456789")) {
            var errorInfo = new HashMap<String, String>();
            errorInfo.put("id",
                    customerService.getMessageSourceValue("com.validation.yakeen.customer", "nin", locale));

            throw AppError.create(AppErrors.BAD_REQUEST, errorInfo, null);
        }

        var identityInfo = new IdentityInfo();
        identityInfo.setNationality(customerInfo.getId());
        identityInfo.setFirstName("Ahmed");
        identityInfo.setLastName("Mohammed");
        identityInfo.setNationality("Saudi");
        identityInfo.setCity("Riyadh");
        identityInfo.setDob(profileRequest.getDob());
        metaInfo.setIdentityInfo(identityInfo);

        // TODO: integrate later
        // send verify request
//         var locale = LocaleContextHolder.getLocale();
//         var identityInfo = customerService.verifyAndGetCustomerInfo(customerInfo);
//         var plan = planService.getPlanDetail(planId);
//         var verifyResponse = customerService.createPhoneVerifyRequest(customerInfo, plan, locale);

        // TODO: change this
        var verifyResponse = new SendOtpResponse();
        verifyResponse.setCode("1234");

        metaInfo.setVerifyInfo(verifyResponse);
    }

    @Override
    public String forType() {
        return Event.CREATE_ACCOUNT.name();
    }

}

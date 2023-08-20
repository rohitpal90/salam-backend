package com.salam.ftth.services.guards;

import com.salam.ftth.model.Event;
import com.salam.ftth.model.RequestContext;
import com.salam.ftth.services.CustomerService;
import com.salam.libs.sm.config.GuardHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class VerifySmsGuard extends GuardHandler {

    @Autowired
    CustomerService customerService;

    @Override
    public void handle(StateContext<String, String> context) {
        var stateMachine = context.getStateMachine();
        var requestContext = RequestContext.<RequestContext>fromStateMachine(stateMachine);
        var verifyCustomerRequest = requestContext.getVerifyCustomerRequest();

        String mobileOtp = verifyCustomerRequest.getOtp();
        customerService.verifyBySms(mobileOtp, requestContext);

        var metaInfo = requestContext.getMeta();
        var identityInfo = metaInfo.getIdentityInfo();

        var customerInfo = metaInfo.getCustomerInfo();
        customerInfo.setFullName(identityInfo.getFirstName() + " " + identityInfo.getLastName());
        customerInfo.setNationality(identityInfo.getNationality());
        customerInfo.setDob(identityInfo.getDob());
        customerInfo.setCity(identityInfo.getCity());
        requestContext.setVerified(true);

        metaInfo.setCustomerInfo(customerInfo);
    }

    @Override
    public String forType() {
        return Event.VERIFY_MOBILE.name();
    }
}

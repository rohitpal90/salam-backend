package com.salam.ftth.services.guards;

import com.salam.ftth.model.Event;
import com.salam.ftth.model.RequestContext;
import com.salam.libs.sm.config.GuardHandler;
import lombok.AllArgsConstructor;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class LocationGuard extends GuardHandler {

    @Override
    public void handle(StateContext<String, String> context) {
        var stateMachine = context.getStateMachine();
        var requestContext = RequestContext.<RequestContext>fromStateMachine(stateMachine);

        var locationInfoRequest = requestContext.getCustomerProfileRequest();
        var metaInfo = requestContext.getMeta();

        var customerInfo = metaInfo.getCustomerInfo();
        customerInfo.setOdbPlateNumber(locationInfoRequest.getOdbPlateNumber());
        customerInfo.setLat(locationInfoRequest.getLat());
        customerInfo.setLng(locationInfoRequest.getLng());
        customerInfo.setProvider(locationInfoRequest.getProvider());

        metaInfo.setCustomerInfo(customerInfo);
    }

    @Override
    public String forType() {
        return Event.ADD_LOCATION.name();
    }
}

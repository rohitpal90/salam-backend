package com.salam.dms.controllers;

import com.salam.dms.config.sm.StateMachineAdapter;
import com.salam.dms.model.Event;
import com.salam.dms.model.EventResult;
import com.salam.dms.model.PlanInfo;
import com.salam.dms.model.RequestContext;
import com.salam.dms.model.request.CustomerProfileRequest;
import com.salam.dms.model.request.VerifyCustomerRequest;
import com.salam.dms.services.RequestService;
import eu.fraho.spring.securityJwt.base.dto.JwtUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final StateMachineAdapter stateMachineAdapter;
    private final RequestService requestService;

    @PostMapping
    public EventResult createCustomerProfile(@RequestBody CustomerProfileRequest profileRequest,
                                             @AuthenticationPrincipal JwtUser user,
                                             PlanInfo planInfo) {
        var requestContext = requestService.initiate(profileRequest, planInfo, user);
        return stateMachineAdapter.trigger(Event.CREATE_ACCOUNT, requestContext).block();
    }

    @PostMapping("/verify")
    public EventResult verifyCustomer(@RequestBody VerifyCustomerRequest request,
                                      @RequestHeader("reqId") RequestContext requestContext) {
        requestContext.setVerifyCustomerRequest(request);
        return stateMachineAdapter.trigger(Event.VERIFY_MOBILE, requestContext).block();
    }

}

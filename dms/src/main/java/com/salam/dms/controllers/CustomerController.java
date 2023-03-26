package com.salam.dms.controllers;

import com.salam.dms.config.sm.StateMachineAdapter;
import com.salam.dms.model.Event;
import com.salam.dms.model.EventResult;
import com.salam.dms.model.PlanInfo;
import com.salam.dms.model.RequestContext;
import com.salam.dms.model.request.CustomerProfileRequest;
import com.salam.dms.model.request.PaymentInfoRequest;
import com.salam.dms.model.request.VerifyCustomerRequest;
import com.salam.dms.services.RequestService;
import eu.fraho.spring.securityJwt.base.dto.JwtUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final StateMachineAdapter stateMachineAdapter;
    private final RequestService requestService;

    @PostMapping
    public EventResult createCustomerProfile(@RequestBody @Valid CustomerProfileRequest profileRequest,
                                             @AuthenticationPrincipal JwtUser user,
                                             @RequestParam(value = "reqId", required = false) String requestId,
                                             PlanInfo planInfo) {
        var requestContext = requestService.initiateOrGet(profileRequest, planInfo, user, requestId);
        requestContext.setCustomerProfileRequest(profileRequest);
        return stateMachineAdapter.trigger(Event.CREATE_ACCOUNT, requestContext).block();
    }

    @PostMapping("/verify")
    public EventResult verifyCustomer(@RequestBody @Valid VerifyCustomerRequest request,
                                      @RequestParam("reqId") RequestContext requestContext) {
        requestContext.setVerifyCustomerRequest(request);
        return stateMachineAdapter.trigger(Event.VERIFY_MOBILE, requestContext).block();
    }

    @PostMapping("/confirm")
    public EventResult confirmRequest(@RequestBody @Valid PaymentInfoRequest paymentInfoRequest,
                                      @RequestParam("reqId") RequestContext requestContext) {
        requestContext.setPaymentInfoRequest(paymentInfoRequest);
        return stateMachineAdapter.trigger(Event.CUSTOMER_CONFIRM, requestContext).block();
    }

    @PostMapping("/cancel")
    public EventResult cancelRequest(@RequestParam("reqId") RequestContext requestContext) {
        return stateMachineAdapter.trigger(Event.CUSTOMER_CANCEL, requestContext).block();
    }

}

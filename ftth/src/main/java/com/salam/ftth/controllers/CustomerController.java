package com.salam.ftth.controllers;

import com.salam.ftth.model.Event;
import com.salam.ftth.model.PlanInfo;
import com.salam.ftth.model.RequestContext;
import com.salam.ftth.model.request.CustomerProfileRequest;
import com.salam.ftth.model.request.VerifyCustomerRequest;
import com.salam.ftth.model.response.EventResult;
import com.salam.ftth.services.StateMachineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final StateMachineService stateMachineService;

    @PostMapping
    public EventResult createCustomerProfile(@RequestBody @Valid CustomerProfileRequest profileRequest,
                                             PlanInfo planInfo) {
        var requestContext = stateMachineService.initiate(profileRequest, planInfo);
        return stateMachineService.trigger(Event.CREATE_ACCOUNT, requestContext);
    }

    @PostMapping("/verify")
    public EventResult verifyCustomer(@RequestBody @Valid VerifyCustomerRequest request,
                                      @RequestParam("reqId") RequestContext requestContext) {
        requestContext.setVerifySmsRequest(request);
        return stateMachineService.trigger(Event.VERIFY_MOBILE, requestContext);
    }

    @PostMapping("/confirm")
    public EventResult confirmRequest(@RequestParam("reqId") RequestContext requestContext) {
        return stateMachineService.trigger(Event.CUSTOMER_CONFIRM, requestContext);
    }

    @PostMapping("/cancel")
    public EventResult cancelRequest(@RequestParam("reqId") RequestContext requestContext) {
        return stateMachineService.trigger(Event.CUSTOMER_CANCEL, requestContext);
    }

}

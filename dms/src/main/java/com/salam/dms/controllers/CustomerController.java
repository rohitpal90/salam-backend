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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/customers")
@Tag(name = "Customer")
public class CustomerController {

    private final StateMachineAdapter stateMachineAdapter;
    private final RequestService requestService;

    @PostMapping
    @Operation(
            summary = "Create or update customer profile",
            responses = {
                    @ApiResponse(responseCode = "401", ref = "UnauthenticatedResponse"),
                    @ApiResponse(responseCode = "400", ref = "BadRequestResponse"),
                    @ApiResponse(responseCode = "404", ref = "NotFoundResponse"),
                    @ApiResponse(responseCode = "200", ref = "CustomerSuccessResponse")
            }
    )
    public EventResult createCustomerProfile(@RequestBody @Valid CustomerProfileRequest profileRequest,
                                             @AuthenticationPrincipal JwtUser user,
                                             @RequestParam(value = "reqId", required = false) String requestId,
                                             PlanInfo planInfo) {
        var requestContext = requestService.initiateOrGet(profileRequest, planInfo, user, requestId);
        requestContext.setCustomerProfileRequest(profileRequest);
        return stateMachineAdapter.trigger(Event.CREATE_ACCOUNT, requestContext).block();
    }

    @PostMapping("/verify")
    @Operation(
            summary = "Verify customer via OTP",
            responses = {
                    @ApiResponse(responseCode = "200", ref = "VerifyResponse"),
                    @ApiResponse(responseCode = "400", ref = "VerifyFailureResponse"),
                    @ApiResponse(responseCode = "400", ref = "InvalidStateResponse"),
                    @ApiResponse(responseCode = "401", ref = "UnauthenticatedResponse"),
                    @ApiResponse(responseCode = "404", ref = "NotFoundResponse")
            }
    )
    public EventResult verifyCustomer(@RequestBody @Valid VerifyCustomerRequest request,
                                      @RequestParam("reqId") RequestContext requestContext) {
        requestContext.setVerifyCustomerRequest(request);
        return stateMachineAdapter.trigger(Event.VERIFY_MOBILE, requestContext).block();
    }

    @PostMapping("/confirm")
    @Operation(
            summary = "Confirm customer request",
            responses = {
                    @ApiResponse(responseCode = "200", ref = "CustomerConfirmResponse"),
                    @ApiResponse(responseCode = "400", content = {
                            @Content(examples = {
                                    @ExampleObject(name = "InvalidStateResponse", ref = "InvalidStateResponse"),
                                    @ExampleObject(name = "BadRequestResponse", ref = "BadRequestResponse")
                            })
                    }),
                    @ApiResponse(responseCode = "401", ref = "UnauthenticatedResponse"),
                    @ApiResponse(responseCode = "404", ref = "NotFoundResponse")
            }
    )
    public EventResult confirmRequest(@RequestBody @Valid PaymentInfoRequest paymentInfoRequest,
                                      @RequestParam("reqId") RequestContext requestContext) {
        requestContext.setPaymentInfoRequest(paymentInfoRequest);
        return stateMachineAdapter.trigger(Event.CUSTOMER_CONFIRM, requestContext).block();
    }

    @PostMapping("/cancel")
    @Operation(
            summary = "Cancel customer request",
            responses = {
                    @ApiResponse(responseCode = "200", ref = "CustomerCancelResponse"),
                    @ApiResponse(responseCode = "400", ref = "InvalidStateResponse"),
                    @ApiResponse(responseCode = "401", ref = "UnauthenticatedResponse"),
                    @ApiResponse(responseCode = "404", ref = "NotFoundResponse")
            }
    )
    public EventResult cancelRequest(@RequestParam("reqId") RequestContext requestContext) {
        return stateMachineAdapter.trigger(Event.CUSTOMER_CANCEL, requestContext).block();
    }

}

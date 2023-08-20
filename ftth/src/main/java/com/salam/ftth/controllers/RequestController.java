package com.salam.ftth.controllers;

import com.salam.ftth.config.exception.AppError;
import com.salam.ftth.config.exception.AppErrors;
import com.salam.ftth.model.PlanInfo;
import com.salam.ftth.model.RequestContext;
import com.salam.ftth.model.States;
import com.salam.ftth.model.request.AppointmentBookRequest;
import com.salam.ftth.model.request.CustomerProfileRequest;
import com.salam.ftth.model.request.VerifyCustomerRequest;
import com.salam.ftth.services.CustomerService;
import com.salam.ftth.services.StateMachineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.salam.ftth.model.Event.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@AllArgsConstructor
@RequestMapping("/requests")
public class RequestController {

    private final StateMachineService stateMachineService;
    private final CustomerService customerService;

    @GetMapping("/summary")
    @Operation(
            summary = "Request Summary",
            responses = {
                    @ApiResponse(responseCode = "200", ref = "SummarySuccessResponse"),
                    @ApiResponse(responseCode = "404", ref = "RequestNotFoundResponse"),
            }
    )
    public Object getReqInfo(@RequestParam("reqId") RequestContext requestContext) {
        var sm = stateMachineService.getSm(requestContext);
        var metaInfo = requestContext.getMeta();

        var summaryInfo = new HashMap<String, Object>() {
            {
                put("reqId", requestContext.getOrderId());
                put("state", sm.getState().getId());
            }
        };

        if (metaInfo.isVerified()) {
            summaryInfo.put("customerInfo", metaInfo.getCustomerInfo());
        }

        if (metaInfo.getAppointment() != null) {
            summaryInfo.put("appointment", metaInfo.getAppointment());
        }

        return summaryInfo;
    }

    @Operation(
            summary = "Create new order",
            responses = {
                    @ApiResponse(responseCode = "200", ref = "CustomerProfileSuccessResponse"),
                    @ApiResponse(responseCode = "400", content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(name = "BadRequestResponse", ref = "BadRequestResponse"),
                            }
                    )),
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = {
                    @ExampleObject(ref = "NewOrderRequest", name = "NewOrderRequest")
            }))
    )
    @PostMapping("/create")
    public Object create(@RequestBody
                         @Validated(value = CustomerProfileRequest.CustomProfileRequestGroup.class)
                         CustomerProfileRequest profileRequest, @RequestParam("planId") String planId) {
        var planInfo = new PlanInfo(planId, null);
        var requestContext = stateMachineService.initiate(profileRequest, planInfo);

        requestContext.setCustomerProfileRequest(profileRequest);
        return stateMachineService.trigger(CREATE_ACCOUNT, requestContext);
    }

    @PostMapping("/verify")
    @Operation(
            summary = "Verify customer via OTP",
            responses = {
                    @ApiResponse(responseCode = "200", ref = "VerifyCustomerResponse"),
                    @ApiResponse(responseCode = "400", content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(name = "BadRequestResponse", ref = "BadRequestResponse"),
                            }
                    )),
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = {
                    @ExampleObject(ref = "VerifyCustomerRequest", name = "VerifyCustomerRequest")
            }))
    )
    public Object verify(@RequestBody @Valid VerifyCustomerRequest verifyCustomerRequest,
                         @RequestParam("reqId") RequestContext requestContext) {
        requestContext.setVerifyCustomerRequest(verifyCustomerRequest);

        var result = stateMachineService.trigger(VERIFY_MOBILE, requestContext);
        if (!result.hasAppError()) {
            var identityInfo = requestContext.getMeta().getIdentityInfo();
            result.setData(identityInfo);
        }

        return result;
    }

    @PostMapping("/contact")
    @Operation(
            summary = "Collect contact info",
            responses = {
                    @ApiResponse(responseCode = "200", ref = "ContactInfoResponse"),
                    @ApiResponse(responseCode = "400", ref = "BadRequestResponse"),
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = {
                    @ExampleObject(ref = "ContactInfoRequest", name = "ContactInfoRequest")
            }))
    )
    public Object contact(@RequestBody
                          @Validated(value = CustomerProfileRequest.ContactInfoRequestGroup.class)
                          CustomerProfileRequest profileRequest,
                          @RequestParam("reqId") RequestContext requestContext) {
        requestContext.setCustomerProfileRequest(profileRequest);
        return stateMachineService.trigger(ADD_CONTACT, requestContext);
    }

    @PostMapping("/location")
    @Operation(
            summary = "Collect Location info",
            responses = {
                    @ApiResponse(responseCode = "200", ref = "LocationInfoResponse"),
                    @ApiResponse(responseCode = "400", ref = "BadRequestResponse"),
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = {
                    @ExampleObject(ref = "LocationInfoRequest", name = "LocationInfoRequest")
            }))
    )
    public Object location(@RequestBody
                           @Validated(CustomerProfileRequest.LocationRequestGroup.class) CustomerProfileRequest profileRequest,
                           @RequestParam("reqId") RequestContext requestContext) {
        requestContext.setCustomerProfileRequest(profileRequest);
        return stateMachineService.trigger(ADD_LOCATION, requestContext);
    }

    @PostMapping("/book")
    @Operation(
            summary = "Book installation appointment",
            responses = {
                    @ApiResponse(responseCode = "200", ref = "AppointmentBookResponse"),
                    @ApiResponse(responseCode = "400", ref = "InvalidStateResponse"),
                    @ApiResponse(responseCode = "404", ref = "NotFoundResponse")
            }
    )
    public Object schedule(@RequestBody @Valid AppointmentBookRequest request,
                           @RequestParam("reqId") RequestContext requestContext) {
        requestContext.setAppointmentBookRequest(request);
        return stateMachineService.trigger(SCHEDULE, requestContext);
    }

    @PostMapping("/approve")
    @Operation(
            summary = "Approve request",
            responses = {
                    @ApiResponse(responseCode = "200", ref = "ApproveRequestResponse"),
                    @ApiResponse(responseCode = "400", ref = "InvalidStateResponse"),
                    @ApiResponse(responseCode = "404", ref = "NotFoundResponse")
            }
    )
    public Object approve(@RequestParam("reqId") RequestContext requestContext) {
        return stateMachineService.trigger(APPROVE, requestContext);
    }

    @Operation(
            summary = "Complete request",
            responses = {
                    @ApiResponse(responseCode = "200", ref = "CompleteRequestResponse"),
                    @ApiResponse(responseCode = "400", ref = "InvalidStateResponse"),
                    @ApiResponse(responseCode = "404", ref = "NotFoundResponse")
            }
    )
    @PostMapping("/complete")
    public Object complete(@RequestParam("reqId") RequestContext requestContext) {
        return stateMachineService.trigger(COMPLETE, requestContext);
    }

    @PostMapping("/cancel")
    @Operation(
            summary = "Cancel request",
            responses = {
                    @ApiResponse(responseCode = "200", ref = "CustomerCancelResponse"),
                    @ApiResponse(responseCode = "400", ref = "InvalidStateResponse"),
                    @ApiResponse(responseCode = "404", ref = "NotFoundResponse")
            }
    )
    public Object cancel(@RequestParam("reqId") RequestContext requestContext) {
        return stateMachineService.trigger(CUSTOMER_CANCEL, requestContext);
    }

    @PostMapping("/register")
    @Operation(
            summary = "Register Customer request",
            responses = {
                    @ApiResponse(responseCode = "200", ref = "CustomerCancelResponse"),
                    @ApiResponse(responseCode = "400", ref = "InvalidStateResponse"),
                    @ApiResponse(responseCode = "404", ref = "NotFoundResponse")
            }
    )
    public Object registerCustomer(@RequestParam("reqId") RequestContext requestContext,
                                   @RequestBody @Validated(value = CustomerProfileRequest.RegisterRequestGroup.class)
                                   CustomerProfileRequest profileRequest) {
        var state = stateMachineService.getSm(requestContext).getState().getId();
        if (!States.COMPLETED.name().equals(state)) {
            throw AppError.create(AppErrors.INVALID_STATE);
        }

        customerService.register(requestContext, profileRequest);
        return Map.of("message", "success");
    }
}

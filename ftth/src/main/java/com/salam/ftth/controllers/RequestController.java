package com.salam.ftth.controllers;

import com.salam.ftth.model.PlanInfo;
import com.salam.ftth.model.RequestContext;
import com.salam.ftth.model.request.CustomerProfileRequest;
import com.salam.ftth.model.request.VerifyCustomerRequest;
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

import static com.salam.ftth.model.Event.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@AllArgsConstructor
@RequestMapping("/requests")
public class RequestController {

    private final StateMachineService stateMachineService;

    @GetMapping("/summary")
    @Operation(
            summary = "Request Summary",
            responses = {
                    @ApiResponse(responseCode = "200", ref = "SummarySuccessResponse"),
                    @ApiResponse(responseCode = "404", ref = "RequestNotFoundResponse"),
                    @ApiResponse(responseCode = "401", ref = "UnauthenticatedResponse"),
            }
    )
    public Object getReqInfo(@RequestParam("reqId") RequestContext requestContext) {
        var restoreContext = stateMachineService.restore(requestContext);
        var metaInfo = restoreContext.getMeta();

        return new HashMap<String, Object>() {
            {
                put("reqId", requestContext.getOrderId());
                put("identityInfo", metaInfo.getIdentityInfo());
            }
        };
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
}

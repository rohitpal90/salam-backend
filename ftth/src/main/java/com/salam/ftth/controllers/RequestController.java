package com.salam.ftth.controllers;

import com.salam.ftth.model.RequestContext;
import com.salam.ftth.services.StateMachineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

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
        var customerInfo = metaInfo.getCustomerInfo();

        return new HashMap<String, Object>() {
            {
                put("reqId", requestContext.getOrderId());
                put("customerInfo", new HashMap<>() {
                    {
                        put("fullName", customerInfo.getFullName());
                        put("mobile", customerInfo.getMobile());
                        put("email", customerInfo.getEmail());
                        put("username", customerInfo.getUsername());
                    }
                });
            }
        };
    }

}

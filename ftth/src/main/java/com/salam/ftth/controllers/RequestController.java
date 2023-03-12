package com.salam.ftth.controllers;

import com.salam.ftth.model.RequestContext;
import com.salam.ftth.services.StateMachineService;
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
    public Object getReqInfo(@RequestParam("reqId") RequestContext requestContext) {
        var restoreContext = stateMachineService.restore(requestContext);
        var metaInfo = restoreContext.getMeta();
        return new HashMap<String, Object>() {
            {
                put("reqId", requestContext.getOrderId());
                put("customerInfo", metaInfo.getCustomerInfo());
                put("plan", metaInfo.getPlanInfo());
                put("appointment", metaInfo.getAppointment());
            }
        };
    }

}

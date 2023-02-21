package com.salam.dms.controllers;

import com.salam.dms.model.RequestContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/requests")
public class RequestController {

    @GetMapping("/summary")
    public Object getReqInfo(@RequestParam("reqId") RequestContext requestContext) {
        var metaInfo = requestContext.getMetaInfo();
        return new HashMap<String, Object>() {
            {
                put("reqId", requestContext.getRequestId());
                put("customerInfo", metaInfo.getCustomerInfo());
                put("plan", metaInfo.getPlanInfo());
                put("appointment", metaInfo.getAppointment());
            }
        };
    }

}

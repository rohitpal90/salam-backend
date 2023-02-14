package com.salam.dms.controllers;

import com.salam.dms.model.RequestContext;
import com.salam.dms.model.RequestMetaInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/requests")
public class RequestController {

    @GetMapping("/summary")
    public Object getReqInfo(@RequestParam("reqId") RequestContext requestContext) {
        var metaInfo = requestContext.getMetaInfo();
        return Map.of(
                "reqId", requestContext.getRequestId(),
                "customerInfo", metaInfo.getCustomerInfo(),
                "plan", metaInfo.getPlanInfo(),
                "appointment", metaInfo.getAppointment()
        );
    }

}

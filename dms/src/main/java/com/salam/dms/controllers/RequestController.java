package com.salam.dms.controllers;

import com.salam.dms.model.RequestContext;
import com.salam.dms.services.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/requests")
public class RequestController {

    @Autowired
    PlanService planService;

    @Value("${billing.vat}")
    Integer planVat;

    @GetMapping("/summary")
    public Object getReqInfo(@RequestParam("reqId") RequestContext requestContext) {
        var metaInfo = requestContext.getMetaInfo();
        var summary = new HashMap<String, Object>() {
            {
                put("reqId", requestContext.getOrderId());
                put("customerInfo", metaInfo.getCustomerInfo());
                put("plan", metaInfo.getPlanInfo());
                put("appointment", metaInfo.getAppointment());
                put("paymentInfo", metaInfo.getPaymentInfo());
            }
        };

        // set billing info
        if (Objects.nonNull(metaInfo.getPlanInfo())) {
            var plan = planService.getPlanDetail(metaInfo.getPlanInfo().getPlanId());
            var planMetaInfo = plan.getMeta();
            var planPrice = planMetaInfo.getPrice();
            var billTotal = parsePlanPrice(planPrice).add(new BigDecimal(planVat));

            summary.put("billingInfo", Map.of(
                    "monthly", planPrice,
                    "vat", String.format("%d SAR", planVat),
                    "total", String.format("%s SAR", billTotal)
            ));
        }

        return summary;
    }

    private BigDecimal parsePlanPrice(String price) {
        return Arrays.stream(price.split(" ")).findFirst()
                .map(BigDecimal::new)
                .get();
    }


}

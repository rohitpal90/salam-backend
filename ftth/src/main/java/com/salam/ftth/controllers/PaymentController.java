package com.salam.ftth.controllers;

import com.salam.ftth.model.RequestContext;
import com.salam.ftth.services.StateMachineService;
import com.salam.libs.feign.payment.client.model.PaymentUpdate;
import com.salam.libs.payment.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final StateMachineService stateMachineService;
    private static final TaskExecutor INVOICE_REFRESH_EXECUTOR = new ConcurrentTaskExecutor();

    @PostMapping("/init")
    public Object initPayment(@RequestParam("reqId") RequestContext requestContext) {
        var response = paymentService.createInvoice(requestContext.getOrderId());
        return new HashMap<>() {{
            put("url", response.getPaymentUrl());
            put("reqId", requestContext.getOrderId());
            put("description", response.getDescription());
        }};
    }

    @GetMapping("/check")
    public Object checkStatus(@RequestParam("reqId") RequestContext requestContext) {
        stateMachineService.restore(requestContext);

        var metaInfo = requestContext.getMeta();
        var paymentInfo = metaInfo.getPaymentInfo();

        var invoiceId = paymentInfo.getInvoiceId();
        INVOICE_REFRESH_EXECUTOR.execute(() -> {
            paymentService.refreshInvoice(invoiceId);
        });

        return Map.of("status", paymentInfo.getStatus());
    }

    @PostMapping("/updates")
    public Object paymentUpdates(@RequestBody PaymentUpdate paymentUpdate) {
        // TODO: verify sign
        paymentService.processUpdate(paymentUpdate);
        return "success";
    }
}

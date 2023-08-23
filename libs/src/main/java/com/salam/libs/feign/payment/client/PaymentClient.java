package com.salam.libs.feign.payment.client;

import com.salam.libs.feign.payment.client.model.InvoiceInfoResponse;
import com.salam.libs.feign.payment.client.model.InvoiceRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment")
public interface PaymentClient {

    @GetMapping(value = "${payment.invoices.get.url}")
    InvoiceInfoResponse getInvoiceById(@PathVariable("invoiceId") String invoiceId);

    @PostMapping(value = "${payment.invoices.create.url}")
    InvoiceInfoResponse createInvoice(@RequestBody InvoiceRequest invoiceRequest);
}

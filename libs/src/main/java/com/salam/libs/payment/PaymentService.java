package com.salam.libs.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salam.libs.feign.payment.client.PaymentClient;
import com.salam.libs.feign.payment.client.model.*;
import com.salam.libs.payment.config.PaymentProperties;
import com.salam.libs.payment.model.PaymentInitRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class PaymentService {

    private final PaymentClient paymentClient;
    private final PaymentProperties paymentProperties;
    private final PaymentProcessorService paymentProcessorService;

    public PaymentService(PaymentClient paymentClient, PaymentProperties paymentProperties,
                          PaymentProcessorService paymentProcessorService) {
        this.paymentClient = paymentClient;
        this.paymentProperties = paymentProperties;
        this.paymentProcessorService = paymentProcessorService;
    }

    public <T> T refreshInvoice(String invoiceId) {
        var invoice = paymentClient.getInvoiceById(invoiceId);
        return paymentProcessorService.processUpdate(invoice, null);
    }

    public <T> T processUpdate(PaymentUpdate paymentUpdate) {
        var invoice = paymentUpdate.getInvoice();
        return paymentProcessorService.processUpdate(invoice, paymentUpdate.getId());
    }

    public InvoiceInfoResponse createInvoice(String reqId, PaymentInitRequest paymentInitRequest) {
        var paymentInfo = paymentProcessorService.initPayment(reqId);
        var paymentInfoAttrs = paymentInfo.getAttrs();

        var invoiceRequest = InvoiceRequest.builder()
                .amount(paymentInfo.getAmount())
                .applicationId(paymentProperties.getApplicationId())
                .description(paymentInfo.getDescription())
                .user(
                        InvoiceRequest.User.builder()
                                .id(paymentInfoAttrs.getEmail())
                                .phone(paymentInfoAttrs.getMobile())
                                .name(paymentInfoAttrs.getName()).build()
                )
                .expiresAt(Instant.now()
                        .plusSeconds(
                                TimeUnit.MINUTES.toSeconds(paymentProperties.getExpirationMinutes()))
                        .toString())
                .returnUrl(paymentInitRequest.getReturnUrl())
                .metadata(
                        new ObjectMapper().createObjectNode().put("reqId", reqId).toString()
                ).build();

        var response = paymentClient.createInvoice(invoiceRequest);

        paymentProcessorService.processNewInvoice(reqId, response);
        return response;
    }

    public InvoiceRefundResponse refundInvoice(String invoiceId, InvoiceRefundRequest invoiceRefundRequest) {
        return null;
    }
}


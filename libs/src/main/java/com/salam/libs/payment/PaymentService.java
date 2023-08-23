package com.salam.libs.payment;

import com.salam.libs.feign.payment.client.PaymentClient;
import com.salam.libs.feign.payment.client.model.*;
import com.salam.libs.payment.config.PaymentProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    public InvoiceInfoResponse createInvoice(String reqId) {
        var paymentInfo = paymentProcessorService.initPayment(reqId);

        var invoiceRequest = InvoiceRequest.builder()
                .amount(paymentInfo.getAmount())
                .applicationId(paymentProperties.getApplicationId())
                .description(paymentInfo.getDescription())
                .metadata(paymentInfo.getAttrs()).build();
        var response = paymentClient.createInvoice(invoiceRequest);

        paymentProcessorService.processNewInvoice(reqId, response);
        return response;
    }

    public InvoiceRefundResponse refundInvoice(String invoiceId, InvoiceRefundRequest invoiceRefundRequest) {
        return null;
    }
}


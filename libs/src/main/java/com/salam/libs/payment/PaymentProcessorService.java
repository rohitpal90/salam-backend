package com.salam.libs.payment;

import com.salam.libs.feign.payment.client.model.InvoiceInfoResponse;
import com.salam.libs.feign.payment.client.model.InvoiceRefundResponse;
import com.salam.libs.payment.model.PaymentRequest;

public interface PaymentProcessorService {
    void processNewInvoice(String reqId, InvoiceInfoResponse invoiceInfoResponse);
    <T> T processUpdate(InvoiceInfoResponse invoiceInfoResponse, String updateId);
    PaymentRequest initPayment(String reqId);
    void initRefund(String invoiceId);
    void processRefund(InvoiceRefundResponse invoiceRefundResponse);
}

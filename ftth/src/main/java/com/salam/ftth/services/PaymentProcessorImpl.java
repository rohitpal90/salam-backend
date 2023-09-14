package com.salam.ftth.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salam.ftth.config.exception.AppError;
import com.salam.ftth.model.PaymentInfo;
import com.salam.ftth.model.RequestContext;
import com.salam.libs.feign.payment.client.model.InvoiceInfoResponse;
import com.salam.libs.feign.payment.client.model.InvoiceRefundResponse;
import com.salam.libs.payment.PaymentProcessorService;
import com.salam.libs.payment.model.PaymentRequest;
import eu.fraho.spring.securityJwt.base.dto.JwtUser;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import static com.salam.ftth.config.exception.AppErrors.FTTH_APP_ERROR;

@Service
@AllArgsConstructor
public class PaymentProcessorImpl implements PaymentProcessorService {

    private final StateMachineService stateMachineService;
    private final PlanService planService;
    private final RequestService requestService;
    private final JwtUser wfUser;
    private final ObjectMapper objectMapper;


    @Override
    public PaymentRequest initPayment(String reqId) {
        var requestContext = restoreReqContext(reqId);
        var metaInfo = requestContext.getMeta();

        var customerInfo = metaInfo.getCustomerInfo();
        var planInfo = metaInfo.getPlanInfo();
        var plan = planService.getPlanById(planInfo.getPlanId());

        var price = plan.getMeta().getPrice().replaceAll(" SAR", "");
        var sm = stateMachineService.getSm(requestContext);

        return PaymentRequest.builder()
                .amount(new BigDecimal(price))
                .description(plan.getMeta().getName())
                .attrs(
                        PaymentRequest.Attrs.builder()
                                .name(customerInfo.getFullName())
                                .email(customerInfo.getEmail())
                                .mobile(customerInfo.getMobile())
                                .state(sm.getState().getId())
                                .reqId(reqId)
                                .build()
                )
                .build();
    }

    @Override
    public void processNewInvoice(String reqId, InvoiceInfoResponse response) {
        var requestContext = restoreReqContext(reqId);
        var metaInfo = requestContext.getMeta();

        var paymentMeta = new HashMap<String, Object>() {{
            put("payments", response.getPayments());
            put("amount", response.getAmount());
            put("createdAt", response.getCreatedAt());
            put("updatedAt", response.getUpdatedAt());
            put("expiresAt", response.getExpiresAt());
        }};

        var paymentInfo = PaymentInfo.builder()
                .status(response.getStatus())
                .invoiceId(response.getId())
                .createdAt(response.getCreatedAt())
                .updatedAt(response.getUpdatedAt())
                .meta(paymentMeta).build();
        metaInfo.setPaymentInfo(paymentInfo);

        requestContext.setMeta(metaInfo);
        stateMachineService.persist(requestContext);
    }

    @Override
    public RequestContext processUpdate(InvoiceInfoResponse invoice, @Nullable String updateId) {
        var metadata = invoice.getMetadata();

        String reqId = "";
        try {
            var metadataJson = objectMapper.readTree(metadata);
            reqId = metadataJson.get("reqId").asText();
        } catch (Exception e) {
            throw AppError.create(FTTH_APP_ERROR);
        }

        var requestContext = requestService.checkRequest(reqId);
        stateMachineService.restore(requestContext);

        var metaInfo = requestContext.getMeta();
        var paymentInfo = metaInfo.getPaymentInfo();

        if (updateId != null) {
            paymentInfo.setLastUpdateId(updateId);
        }

        // update info
        paymentInfo.setStatus(invoice.getStatus());
        paymentInfo.setUpdatedAt(new Date());
        requestContext.setMeta(metaInfo);

        stateMachineService.persist(requestContext);
        return requestContext;
    }

    @Override
    public void initRefund(String invoiceId) {

    }

    @Override
    public void processRefund(InvoiceRefundResponse refundResponse) {

    }

    private RequestContext restoreReqContext(String reqId) {
        var userId = wfUser.getId();
        return stateMachineService.restore(new RequestContext(reqId, userId));
    }
}

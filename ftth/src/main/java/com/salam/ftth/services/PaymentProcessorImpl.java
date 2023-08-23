package com.salam.ftth.services;

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
import java.util.Optional;

import static com.salam.ftth.config.exception.AppErrors.REQUEST_NOT_FOUND;

@Service
@AllArgsConstructor
public class PaymentProcessorImpl implements PaymentProcessorService {

    private final StateMachineService stateMachineService;
    private final PlanService planService;
    private final RequestService requestService;
    private final JwtUser wfUser;


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
                .attrs(new HashMap<>() {{
                    put("name", customerInfo.getFullName());
                    put("email", customerInfo.getEmail());
                    put("mobile", customerInfo.getMobile());
                    put("state", sm.getState().getId());
                    put("reqId", reqId);
                }})
                .build();
    }

    @Override
    public void processNewInvoice(String reqId, InvoiceInfoResponse response) {
        var requestContext = restoreReqContext(reqId);
        var metaInfo = requestContext.getMeta();

        var paymentMeta = new HashMap<String, Object>() {{
            put("paymentMethod", response.getPaymentMethod());
            put("amount", response.getAmount());
            put("applicationId", response.getApplicationId());
        }};
        paymentMeta.putAll(Optional.of(response.getMetadata()).orElseGet(HashMap::new));

        var now = new Date();
        var paymentInfo = PaymentInfo.builder()
                .status(response.getStatus())
                .invoiceId(response.getInvoiceId())
                .createdAt(now)
                .updatedAt(now)
                .meta(paymentMeta).build();
        metaInfo.setPaymentInfo(paymentInfo);

        requestContext.setMeta(metaInfo);
        stateMachineService.persist(requestContext);
    }

    @Override
    public RequestContext processUpdate(InvoiceInfoResponse invoice, @Nullable String updateId) {
        var invoiceMetadata = invoice.getMetadata();
        if (!invoiceMetadata.containsKey("reqId")) {
            throw AppError.create(REQUEST_NOT_FOUND);
        }

        var reqId = ((String) invoiceMetadata.get("reqId"));
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

package com.salam.dms.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salam.dms.adapter.feign.client.CustomerClient;
import com.salam.dms.adapter.feign.mock.ClientMockAdapter;
import com.salam.dms.config.exception.AppError;
import com.salam.dms.config.exception.AppErrors;
import com.salam.dms.db.entity.Plan;
import com.salam.dms.model.IdentityInfo;
import com.salam.dms.model.RequestContext;
import com.salam.dms.model.request.CustomerProfileRequest;
import com.salam.libs.feign.elm.client.AbsherClient;
import com.salam.libs.feign.elm.client.YakeenClient;
import com.salam.libs.feign.elm.model.EntityDto;
import com.salam.libs.feign.elm.model.ErrorSalamResponse;
import com.salam.libs.feign.elm.model.SendOtpRequest;
import com.salam.libs.feign.elm.model.SendOtpResponse;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.salam.dms.config.exception.AppErrors.CUSTOMER_OTP_INVALID;

@Slf4j
@Service
public class CustomerService {

    @Autowired
    CustomerClient customerClient;

    @Autowired
    ClientMockAdapter clientMockAdapter;

    @Autowired
    YakeenClient yakeenClient;

    @Autowired
    AbsherClient absherClient;

    @Value("${app.custom.otp.reason}")
    String otpRequestDefaultReason;

    @Autowired
    private MessageSource messageSource;


    public SendOtpResponse createPhoneVerifyRequest(CustomerProfileRequest request, Plan plan,
                                                    Locale locale) {
        var response = absherClient.sendOtpRequest(
                SendOtpRequest.builder()
                        .operatorId(request.getId())
                        .customerId(request.getId())
                        .language(locale.getLanguage())
                        .reason(otpRequestDefaultReason)
                        .packageName(plan.getMeta().getName())
                        .build()
        );

        return response.getData();
    }

    public boolean verifyBySms(String otp, RequestContext requestContext) {
        var metaInfo = requestContext.getMetaInfo();
        var verifyResponse = metaInfo.getVerifyInfo();

        if (!otp.equals(verifyResponse.getCode())) {
            throw AppError.create(CUSTOMER_OTP_INVALID);
        }

        return true;
    }

    public boolean changeCustomerPhone(String mobile, CustomerProfileRequest request,
                                       RequestContext requestContext) {
        request.setMobile(mobile);
        // TODO: set verified to false

        return true;
    }

    public IdentityInfo verifyAndGetCustomerInfo(CustomerProfileRequest customerInfo) {
        var nin = customerInfo.getId();
        var dateOfBirth = customerInfo.getDob();

        var locale = LocaleContextHolder.getLocale();
        var language = locale.getLanguage();

        var isCitizen = nin.startsWith("1");
        EntityDto entityDto;
        try {
            var identityInfoResponse = isCitizen ?
                    yakeenClient.getCitizenInfo(nin, dateOfBirth) :
                    yakeenClient.getExpatInfo(nin, dateOfBirth);

            entityDto = identityInfoResponse.getData();
        } catch (FeignException e) {
            handleVerifyErrorIfRequired(e, locale);
            throw e;
        }

        var addressesResponse = isCitizen ?
                yakeenClient.getCitizenAddresses(nin, dateOfBirth, language) :
                yakeenClient.getExpatsIqamaNumberAddresses(nin, dateOfBirth, language);
        var addresses = addressesResponse.getData();

        return new IdentityInfo(entityDto, addresses);
    }

    private void handleVerifyErrorIfRequired(FeignException e, Locale locale) {
        int status = e.status();
        var messagePrefix = "com.validation.yakeen.customer";

        if (status == HttpStatus.BAD_REQUEST.value()) {
            var errorResponse = e.responseBody().map(byteBuffer -> {
                try {
                    return new ObjectMapper().readValue(byteBuffer.array(), ErrorSalamResponse.class);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });

            var errorInfo = new HashMap<>();
            if (errorResponse.isPresent()) {
                var errors = errorResponse.get().getErrors();
                if (errors.containsKey("nin")) {
                    errorInfo.put("id", getMessageSourceValue(messagePrefix, "nin", locale));
                }

                if (errors.containsKey("dateOfBirth")) {
                    errorInfo.put("dob", getMessageSourceValue(messagePrefix, "dateOfBirth", locale));
                }
            }

            throw AppError.create(AppErrors.BAD_REQUEST, errorInfo, null);
        }

        if (status == HttpStatus.NOT_FOUND.value()) {
            var errorInfo = Map.of("id", getMessageSourceValue(messagePrefix, "nin", locale));
            throw AppError.create(AppErrors.BAD_REQUEST, errorInfo, null);
        }
    }

    private String getMessageSourceValue(String prefix, String value, Locale locale) {
        return messageSource.getMessage(
                String.join(".", prefix, value),
                null,
                locale
        );
    }
}

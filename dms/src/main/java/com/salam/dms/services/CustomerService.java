package com.salam.dms.services;

import com.salam.dms.adapter.feign.client.CustomerClient;
import com.salam.dms.adapter.feign.mock.ClientMockAdapter;
import com.salam.dms.config.exception.AppError;
import com.salam.dms.db.entity.Plan;
import com.salam.dms.model.RequestContext;
import com.salam.dms.model.request.CustomerProfileRequest;
import com.salam.libs.feign.elm.client.AbsherClient;
import com.salam.libs.feign.elm.client.YakeenClient;
import com.salam.libs.feign.elm.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

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

    public EntityDto createNinVerifyRequest(CustomerProfileRequest customerInfo) {
        var nin = customerInfo.getId();
        var dateOfBirth = customerInfo.getDob();

        SalamSuccessResponse<EntityDto> citizenInfo = yakeenClient.getCitizenInfo(nin, dateOfBirth);
        return citizenInfo.getData();
    }

    public List<AddressDto> getCustomerAddresses(CustomerProfileRequest customerInfo,
                                                                       Locale locale) {
        var nin = customerInfo.getId();
        var dateOfBirth = customerInfo.getDob();

        var response = yakeenClient.getCitizenAddresses(nin, dateOfBirth, locale.getLanguage());
        return response.getData();
    }
}

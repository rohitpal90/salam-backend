package com.salam.dms.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.salam.dms.adapter.feign.client.CustomerClient;
import com.salam.dms.adapter.feign.mock.ClientMockAdapter;
import com.salam.dms.adapter.model.request.VerifySmsRequest;
import com.salam.dms.adapter.model.response.VerifyBySmsResponse;
import com.salam.dms.config.exception.AppError;
import com.salam.dms.model.RequestContext;
import com.salam.dms.model.request.CustomerProfileRequest;
import com.salam.libs.feign.elm.client.AbsherClient;
import com.salam.libs.feign.elm.client.YakeenClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    public VerifyBySmsResponse createPhoneVerifyRequest(String mobile) {
        var verifyRequest = VerifySmsRequest.createByAccNbr(mobile);
//        return customerClient.verifyBySms(verifyRequest);
        return clientMockAdapter.getFor("verifysms", new TypeReference<>() {});
    }

    public boolean verifyBySms(String otp, RequestContext requestContext) {
        var metaInfo = requestContext.getMetaInfo();
        var verifyResponse = metaInfo.getVerifyInfo();

        if (!otp.equals(verifyResponse.getCaptchaCode())) {
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

}

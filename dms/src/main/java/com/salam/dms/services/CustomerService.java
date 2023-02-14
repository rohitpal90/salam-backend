package com.salam.dms.services;

import com.salam.dms.adapter.feign.client.CustomerClient;
import com.salam.dms.adapter.model.request.VerifySmsRequest;
import com.salam.dms.adapter.model.response.VerifyBySmsResponse;
import com.salam.dms.model.RequestContext;
import com.salam.dms.model.request.CustomerProfileRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomerService {

    @Autowired
    CustomerClient customerClient;


    public VerifyBySmsResponse createPhoneVerifyRequest(String mobile) {
        var verifyRequest = VerifySmsRequest.createByAccNbr(mobile);
        return customerClient.verifyBySms(verifyRequest);
    }

    public boolean verifyBySms(String otp, RequestContext requestContext) {
        var metaInfo = requestContext.getMetaInfo();
        var verifyResponse = metaInfo.getVerifyBySmsResponse();

        if (otp.equals(verifyResponse.getCaptchaCode())) {
            return true;
        } else {
            throw new RuntimeException("Invalid captcha");
        }
    }

    public boolean changeCustomerPhone(String mobile, CustomerProfileRequest request,
                                       RequestContext requestContext) {
        request.setMobile(mobile);
        // TODO: set verified to false

        return true;
    }

}

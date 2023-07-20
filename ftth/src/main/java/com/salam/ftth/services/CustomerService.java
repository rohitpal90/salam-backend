package com.salam.ftth.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salam.ftth.adapter.feign.client.CustomerClient;
import com.salam.ftth.adapter.feign.mock.ClientMockAdapter;
import com.salam.ftth.adapter.model.request.VerifySmsRequest;
import com.salam.ftth.adapter.model.response.VerifyBySmsResponse;
import com.salam.ftth.config.exception.AppError;
import com.salam.ftth.model.RequestContext;
import com.salam.ftth.model.request.CustomerProfileRequest;
import com.salam.ftth.model.response.CustomerSubscription;
import com.salam.ftth.repos.UserRepository;
import eu.fraho.spring.securityJwt.base.dto.JwtUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

import static com.salam.ftth.config.exception.AppErrors.CUSTOMER_OTP_INVALID;

@Slf4j
@Service
@AllArgsConstructor
public class CustomerService {

    final CustomerClient customerClient;
    final UserRepository userRepository;
    final ClientMockAdapter clientMockAdapter;


    public VerifyBySmsResponse createPhoneVerifyRequest(String mobile) {
        var verifyRequest = VerifySmsRequest.createByAccNbr(mobile);
//        return customerClient.verifyBySms(verifyRequest);
        return clientMockAdapter.getFor("verifysms", new TypeReference<>() {
        });
    }

    public boolean verifyBySms(String otp, RequestContext requestContext) {
        var metaInfo = requestContext.getMeta();
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

    public List<CustomerSubscription> getCustomerSubscriptions(JwtUser user,
                                                               Pageable pageable) {
        var metaMapper = new ObjectMapper();
        return userRepository.getCustomerSubscriptions(user.getId(), pageable)
                .stream()
                .map(row -> {
                    JsonNode meta;
                    try {
                        meta = metaMapper.readTree((String) row[2]);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }

                    return new CustomerSubscription((String) row[0], (String) row[1],
                            meta, ((Timestamp) row[3]).toInstant().toString());
                }).toList();
    }

}

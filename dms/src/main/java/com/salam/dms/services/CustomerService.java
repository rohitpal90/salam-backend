package com.salam.dms.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salam.dms.adapter.feign.client.AbClient;
import com.salam.dms.adapter.feign.client.CusClient;
import com.salam.dms.adapter.feign.client.CustomerClient;
import com.salam.dms.adapter.feign.mock.ClientMockAdapter;
import com.salam.dms.config.exception.AppError;
import com.salam.dms.config.exception.AppErrors;
import com.salam.dms.db.entity.Plan;
import com.salam.dms.model.RequestContext;
import com.salam.dms.model.request.CustomerProfileRequest;
import com.salam.libs.feign.elm.client.AbsherClient;
import com.salam.libs.feign.elm.client.YakeenClient;
import com.salam.libs.feign.elm.model.*;
import feign.FeignException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

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

    @SneakyThrows
    public EntityDto createNinVerifyRequest(CustomerProfileRequest customerInfo) {
        var nin = customerInfo.getId();
        var dateOfBirth = customerInfo.getDob();
        try {
            var citizenInfo = new SalamSuccessResponse<EntityDto>();
            if(customerInfo.getId().startsWith("1")) {
                citizenInfo = yakeenClient.getCitizenInfo(nin, dateOfBirth);
            }
            if(customerInfo.getId().startsWith("2")){
                citizenInfo = yakeenClient.getExpatInfo(nin, dateOfBirth);
            }
            return citizenInfo.getData();
        }catch (FeignException e){
            handleException(e,customerInfo);
        }
        return new EntityDto();
    }

    @SneakyThrows
    public List<AddressDto> getCustomerAddresses(CustomerProfileRequest customerInfo,
                                                                       Locale locale) {
        var nin = customerInfo.getId();
        var dateOfBirth = customerInfo.getDob();
        try {
            var response = new SalamSuccessResponse<List<AddressDto>>();
            if(customerInfo.getId().startsWith("1")) {
                 response = yakeenClient.getCitizenAddresses(nin, dateOfBirth, locale.getLanguage());
            }
            if(customerInfo.getId().startsWith("2")){
                 response = yakeenClient.getExpatsIqamaNumberAddresses(nin, dateOfBirth, locale.getLanguage());
            }
            return response.getData();
        }catch (FeignException exception){
            handleException(exception,customerInfo);
        }
         return new ArrayList<>();
    }
    private void handleException(FeignException e, CustomerProfileRequest customerInfo) throws IOException {
        int status = e.status();
        if(status == 400){
            ByteBuffer byteBuffer = e.responseBody().get();
            ErrorSalamResponse response = new ObjectMapper().readValue(byteBuffer.array(),ErrorSalamResponse.class);
            Map<String,List<String>> errorList = response.getErrors();
            var idMessage = messageSource.getMessage("id",null,LocaleContextHolder.getLocale());
            var dobMessage = messageSource.getMessage("dob",null,LocaleContextHolder.getLocale());
            Map<String,String> errorMap = new HashMap<>();
            errorList.forEach((k,v)->{
                if(k.equals("dateofBirth")){
                    errorMap.put("dob",dobMessage);
                }
                if(k.equals("nin")){
                    errorMap.put("id",idMessage);
                }
            });
            throw AppError.create(AppErrors.BAD_REQUEST,errorMap,null);
        }
    }
}

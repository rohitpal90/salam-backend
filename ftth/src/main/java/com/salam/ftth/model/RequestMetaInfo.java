package com.salam.ftth.model;

import com.salam.ftth.adapter.model.Appointment;
import com.salam.ftth.model.request.CustomerProfileRequest;
import com.salam.libs.feign.elm.model.SendOtpResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestMetaInfo {
    private CustomerProfileRequest customerInfo;
    private boolean isVerified = false;
    private Appointment appointment;
    private PlanInfo planInfo;
    private SendOtpResponse verifyInfo;
    private IdentityInfo identityInfo;
    private PaymentInfo paymentInfo;

    public RequestMetaInfo(CustomerProfileRequest profileRequest) {
        this.customerInfo = profileRequest;
    }

    public void setIdentityInfo(IdentityInfo identityInfo) {
        this.identityInfo = identityInfo;
    }
}

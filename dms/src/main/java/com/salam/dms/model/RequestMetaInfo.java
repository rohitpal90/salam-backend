package com.salam.dms.model;

import com.salam.dms.adapter.model.Appointment;
import com.salam.dms.adapter.model.response.VerifyBySmsResponse;
import com.salam.dms.model.request.CustomerProfileRequest;
import com.salam.dms.model.request.PaymentInfoRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestMetaInfo {
    private CustomerProfileRequest customerInfo;
    private boolean isVerified = false;
    private Appointment appointment;
    private PlanInfo planInfo;
    private VerifyBySmsResponse verifyInfo;
    private PaymentInfoRequest paymentInfo;

    public RequestMetaInfo(CustomerProfileRequest profileRequest) {
        this.customerInfo = profileRequest;
    }
}

package com.salam.ftth.model;

import com.salam.ftth.adapter.model.Appointment;
import com.salam.ftth.adapter.model.response.VerifyBySmsResponse;
import com.salam.ftth.model.request.CustomerProfileRequest;
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

    public RequestMetaInfo(CustomerProfileRequest profileRequest) {
        this.customerInfo = profileRequest;
    }
}

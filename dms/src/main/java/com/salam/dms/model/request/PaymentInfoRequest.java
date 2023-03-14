package com.salam.dms.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class PaymentInfoRequest {
    private PaymentMethod paymentMethod;

    @AllArgsConstructor
    enum PaymentMethod {
        sadad
    }
}

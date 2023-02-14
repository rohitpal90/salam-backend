package com.salam.dms.adapter.model.request;

import lombok.Data;

@Data
public class VerifySmsRequest {
    private String accNbr;

    public static VerifySmsRequest createByAccNbr(String accNbr) {
        var request = new VerifySmsRequest();
        request.setAccNbr(accNbr);
        return request;
    }
}

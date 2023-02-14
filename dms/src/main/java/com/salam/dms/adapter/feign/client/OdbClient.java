package com.salam.dms.adapter.feign.client;

import com.salam.dms.adapter.model.*;
import com.salam.dms.adapter.model.request.NidRequest;
import com.salam.dms.adapter.model.request.PlateNoRequest;
import com.salam.dms.adapter.model.response.CustomerResponse;
import com.salam.dms.adapter.model.response.PlateNoResponse;
import com.salam.dms.adapter.model.response.SalamNidResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public interface OdbClient {

    @GetMapping(value = "${fttx.mock.salamcheckplateno.url}")
    PlateNoResponse checkBySalamPlateNo(@RequestHeader(value = "Authorization") String token,
                                        PlateNoRequest plateNoReqObj);
    @PostMapping(value = "${fttx.mock.customer.login.api.url}")
    ResponseEntity<CustomerResponse> fetchToken(@RequestBody CustomerDetail customerDetail);

    @PostMapping(value = "${fttx.mock.reg.salamchecknid.url}")
    SalamNidResponse checkBySalamNid(@RequestHeader(value = "Authorization") String token,
                                     NidRequest nidRequest);

}

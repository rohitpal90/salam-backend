package com.salam.dms.adapter.feign.client;

import com.salam.dms.adapter.model.request.VerifySmsRequest;
import com.salam.dms.adapter.model.response.VerifyByEmailResponse;
import com.salam.dms.adapter.model.response.VerifyBySmsResponse;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "customer")
public interface CustomerClient {

    @PostMapping(value = "${customer.verifysms.url}")
    VerifyBySmsResponse verifyBySms(@RequestBody VerifySmsRequest smsRequest);

    @GetMapping
    VerifyByEmailResponse verifyByEmail(@Param String token);

}

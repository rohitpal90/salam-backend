package com.salam.libs.feign.elm.client;

import com.salam.libs.feign.elm.model.SendOtpRequest;
import com.salam.libs.feign.elm.model.SendOtpResponseSuccessSalamResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "absher")
public interface AbsherClient {

    @PostMapping(value = "${absher.otp.url}")
    SendOtpResponseSuccessSalamResponse sendOtpRequest(@RequestBody SendOtpRequest request);
}

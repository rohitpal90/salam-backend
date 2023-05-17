package com.salam.libs.feign;

import com.salam.libs.sm.model.SendOtpRequest;
import com.salam.libs.sm.model.SendOtpResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "${host.name}"+""+"absher")
public interface AbsherClient {

    @PostMapping(value = "${absher.getotp.url}")
    SendOtpResponse getOtp(@SpringQueryMap SendOtpRequest request);
}

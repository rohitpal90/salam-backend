package com.salam.libs.Feign;

import com.salam.libs.sm.model.SendOtpRequest;
import com.salam.libs.sm.model.SendOtpResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "otp",url = "http://localhost:7044/absher/")
public interface OtpClient {

    @PostMapping(value = "/otp")
    SendOtpResponse getOtp(@SpringQueryMap SendOtpRequest request);
}

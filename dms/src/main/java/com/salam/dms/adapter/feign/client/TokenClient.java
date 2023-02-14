package com.salam.dms.adapter.feign.client;

import com.salam.dms.adapter.model.response.CustomerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@FeignClient(name = "token")
public interface TokenClient {

    @GetMapping("${token.fetch.url}")
    CustomerResponse getToken(Map<String, String> body);

}

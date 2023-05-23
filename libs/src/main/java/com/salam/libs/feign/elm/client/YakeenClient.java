package com.salam.libs.feign.elm.client;

import com.salam.libs.feign.elm.model.Citizen;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "yakeen")
public interface YakeenClient {

    @GetMapping(value = "${yakeen.getcitizen.url}")
    Citizen getCitizen(@PathVariable String nin, @RequestParam String dateOfBirth);
}

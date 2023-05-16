package com.salam.libs.Feign;

import com.salam.libs.sm.model.Citizen;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "citizen",url = "http://localhost:7044/yakeen/")
public interface CitizenClient {
    @GetMapping(value = "/citizens/")
    Citizen getCitizen(@PathVariable String nin, @RequestParam String dateOfBirth);
}

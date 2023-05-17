package com.salam.libs.feign;

import com.salam.libs.sm.model.Citizen;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "${host.name}"+""+"yakeen")
public interface YakeenClient {
    @GetMapping(value = "${yakeen.citizen.url}")
    Citizen getCitizen(@PathVariable String nin, @RequestParam String dateOfBirth);
}

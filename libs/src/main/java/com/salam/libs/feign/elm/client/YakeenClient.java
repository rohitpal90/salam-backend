package com.salam.libs.feign.elm.client;

import com.salam.libs.feign.elm.model.AddressDtoListSuccessSalamResponse;
import com.salam.libs.feign.elm.model.EntityDtoSuccessSalamResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "yakeen")
public interface YakeenClient {

    @GetMapping(value = "${yakeen.citizen.url}")
    EntityDtoSuccessSalamResponse getCitizenInfo(@PathVariable String nin, @RequestParam String dateOfBirth);

    @GetMapping(value = "${yakeen.expats.url}")
    EntityDtoSuccessSalamResponse getExpatInfo(@PathVariable String iqamaNumber, @RequestParam String dateOfBirth);

    @GetMapping(value = "${yakeen.citizen.addresses.url}")
    AddressDtoListSuccessSalamResponse getCitizenAddresses(@PathVariable String nin,
                                                           @RequestParam String dateOfBirth,
                                                           @RequestParam String language);

    @GetMapping(value = "${yakeen.expats.addresses.url}")
    AddressDtoListSuccessSalamResponse getExpatsIqamaNumberAddresses(@PathVariable String nin,
                                                                     @RequestParam String dateOfBirth,
                                                                     @RequestParam String language);
}

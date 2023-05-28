package com.salam.libs.feign.elm.client;

import com.salam.libs.feign.elm.model.AddressDto;
import com.salam.libs.feign.elm.model.EntityDto;
import com.salam.libs.feign.elm.model.SalamSuccessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "yakeen")
public interface YakeenClient {

    @GetMapping(value = "${yakeen.citizen.url}")
    SalamSuccessResponse<EntityDto> getCitizenInfo(@PathVariable String nin, @RequestParam String dateOfBirth);

    @GetMapping(value = "${yakeen.expats.url}")
    SalamSuccessResponse<EntityDto> getExpatInfo(@PathVariable String iqamaNumber, @RequestParam String dateOfBirth);

    @GetMapping(value = "${yakeen.citizen.addresses.url}")
    SalamSuccessResponse<List<AddressDto>> getCitizenAddresses(@PathVariable("nin") String nin,
                                                               @RequestParam String dateOfBirth,
                                                               @RequestParam String language);

    @GetMapping(value = "${yakeen.expats.addresses.url}")
    SalamSuccessResponse<List<AddressDto>> getExpatsIqamaNumberAddresses(@PathVariable("iqamaNumber") String iqamaNumber,
                                                                     @RequestParam String dateOfBirth,
                                                                     @RequestParam String language);
}

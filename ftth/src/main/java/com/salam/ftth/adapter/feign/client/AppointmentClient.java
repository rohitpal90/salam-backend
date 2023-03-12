package com.salam.ftth.adapter.feign.client;

import com.salam.ftth.adapter.model.request.AppointmentRequest;
import com.salam.ftth.adapter.model.response.AppointmentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "appointment")
public interface AppointmentClient {

    @GetMapping(value = "${appointment.queryappointment.url}")
    AppointmentResponse fetchAppointmentSlots(@SpringQueryMap AppointmentRequest request);

}

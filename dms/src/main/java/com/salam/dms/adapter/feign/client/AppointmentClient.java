package com.salam.dms.adapter.feign.client;

import com.salam.dms.adapter.model.request.AppointmentRequest;
import com.salam.dms.adapter.model.response.AppointmentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "appointment")
public interface AppointmentClient {

    @GetMapping(value = "${appointment.queryappointment.url}")
    AppointmentResponse fetchAppointmentSlots(@SpringQueryMap AppointmentRequest request);

}

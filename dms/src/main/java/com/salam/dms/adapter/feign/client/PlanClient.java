package com.salam.dms.adapter.feign.client;

import com.salam.dms.adapter.model.response.PlansMockResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface PlanClient {

    @RequestMapping(method = RequestMethod.GET, value = "${fetch_sub_plan_uri}")
    List<PlansMockResponse> fetchV1Plans(@RequestParam String provider, @RequestParam String servType);

}

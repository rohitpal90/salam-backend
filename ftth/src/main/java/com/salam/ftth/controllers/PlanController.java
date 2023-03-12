package com.salam.ftth.controllers;

import com.salam.ftth.db.entity.Plan;
import com.salam.ftth.model.request.PlanFilterRequest;
import com.salam.ftth.services.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/plans")
public class PlanController {

    @Autowired
    PlanService planService;


    @GetMapping
    public List<Plan> getPlans(PlanFilterRequest filter, Pageable pageable) {
        return planService.getPlans(filter, pageable);
    }

}

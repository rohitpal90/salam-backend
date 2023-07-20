package com.salam.ftth.controllers;

import com.salam.ftth.db.entity.Plan;
import com.salam.ftth.model.request.PlanFilterRequest;
import com.salam.ftth.services.PlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/plans")
@Tag(name = "Plan")
public class PlanController {

    @Autowired
    PlanService planService;


    @GetMapping
    @Operation(
            summary = "Get plans, filter by planId, category or type",
            responses = {
                    @ApiResponse(responseCode = "200", ref = "PlanResponse"),
            }
    )
    public List<Plan> getPlans(PlanFilterRequest filter, Pageable pageable) {
        var pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        return planService.getPlans(filter, pageRequest);
    }

}

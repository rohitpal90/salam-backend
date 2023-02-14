package com.salam.dms.controllers;

import com.salam.dms.db.entity.Plan;
import com.salam.dms.model.request.PlanFilterRequest;
import com.salam.dms.services.PlanService;
import eu.fraho.spring.securityJwt.base.dto.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public List<Plan> getPlanByDealerId(@AuthenticationPrincipal JwtUser user,
                                        PlanFilterRequest filter, Pageable pageable) {
        return planService.getDealerPlans(user.getId(), filter, pageable);
    }

}

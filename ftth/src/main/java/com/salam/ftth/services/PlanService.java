package com.salam.ftth.services;

import com.salam.ftth.config.exception.AppError;
import com.salam.ftth.db.entity.Plan;
import com.salam.ftth.model.request.PlanFilterRequest;
import com.salam.ftth.repos.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.salam.ftth.config.exception.AppErrors.PLAN_NOT_FOUND;


@Service
public class PlanService {

    @Autowired
    PlanRepository planRepository;


    public List<Plan> getPlans(PlanFilterRequest filter, Pageable pageable) {
        return planRepository.findPlans(filter, pageable);
    }

    public void checkPlan(String planId) {
        if (!planRepository.existsByPlanId(planId)) {
            throw AppError.create(PLAN_NOT_FOUND);
        }
    }

    public Plan getPlanById(String planId) {
        return planRepository.getPlanById(planId)
                .orElseThrow(() -> AppError.create(PLAN_NOT_FOUND));
    }
}

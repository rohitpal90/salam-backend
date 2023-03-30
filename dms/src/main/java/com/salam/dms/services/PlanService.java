package com.salam.dms.services;

import com.salam.dms.config.exception.AppError;
import com.salam.dms.db.entity.Plan;
import com.salam.dms.model.request.PlanFilterRequest;
import com.salam.dms.repos.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.salam.dms.config.exception.AppErrors.PLAN_NOT_FOUND;


@Service
public class PlanService {

    @Autowired
    PlanRepository planRepository;


    public List<Plan> getDealerPlans(Long dealerId, PlanFilterRequest filter, Pageable pageable) {
        return planRepository.findDealerPlans(dealerId, filter, pageable);
    }

    public void checkDealerPlan(String planId, Long dealerId) {
        if (!planRepository.existsByPlanIdAndDealerId(planId, dealerId)) {
            throw AppError.create(PLAN_NOT_FOUND);
        }
    }

    public Plan getPlanDetail(String planId) {
        var planOpt =  planRepository.findById(Long.valueOf(planId));
        if (planOpt.isEmpty()) {
            throw AppError.create(PLAN_NOT_FOUND);
        }

        return planOpt.get();
    }
}

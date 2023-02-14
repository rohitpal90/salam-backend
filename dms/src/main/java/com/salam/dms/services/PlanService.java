package com.salam.dms.services;

import com.salam.dms.config.exception.AppError;
import com.salam.dms.config.exception.AppErrors;
import com.salam.dms.db.entity.Plan;
import com.salam.dms.model.request.PlanFilterRequest;
import com.salam.dms.repos.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.salam.dms.config.exception.AppErrors.NOT_FOUND;


@Service
public class PlanService {

    @Autowired
    PlanRepository planRepository;


    public List<Plan> getDealerPlans(Long dealerId, PlanFilterRequest filter, Pageable pageable) {
        return planRepository.findDealerPlans(dealerId, filter, pageable);
    }

    public void checkDealerPlan(String planId, Long dealerId) {
        if (!planRepository.existsByPlanIdAndDealerId(planId, dealerId)) {
            throw AppError.create("Plan not found", NOT_FOUND);
        }
    }
}

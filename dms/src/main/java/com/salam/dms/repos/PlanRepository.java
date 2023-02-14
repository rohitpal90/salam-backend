package com.salam.dms.repos;

import com.salam.dms.db.entity.Plan;
import com.salam.dms.model.request.PlanFilterRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanRepository extends PagingAndSortingRepository<Plan, Long> {

    @Query(value = "select p.* from plan p join dealer_plan dp on dp.plan_id = p.id " +
            "where dp.dealer_id =:dealerId and " +
            "((JSON_VALUE(p.meta, '$.category') = :#{#f.category} or (:#{#f.category} is null)) and " +
            "(JSON_VALUE(p.meta, '$.planType') = :#{#f.type} or (:#{#f.type} is null)) and " +
            "(p.id = :#{#f.planId} or (:#{#f.planId} is null))) ", nativeQuery = true)
    List<Plan> findDealerPlans(Long dealerId, PlanFilterRequest f, Pageable pageable);

    @Query(value = "select case when count(1) > 0 then 'true' else 'false' end from plan p " +
            "join dealer_plan dp on dp.plan_id = p.id " +
            "where dp.dealer_id = :dealerId and p.id = :planId", nativeQuery = true)
    boolean existsByPlanIdAndDealerId(String planId, Long dealerId);
}

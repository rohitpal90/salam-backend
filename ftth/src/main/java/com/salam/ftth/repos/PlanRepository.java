package com.salam.ftth.repos;

import com.salam.ftth.db.entity.Plan;
import com.salam.ftth.model.request.PlanFilterRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanRepository extends PagingAndSortingRepository<Plan, Long> {

    @Query(value = "select p.* from plans p where " +
            "((JSON_VALUE(p.meta, '$.category') = :#{#f.category} or (:#{#f.category} is null)) and " +
            "(JSON_VALUE(p.meta, '$.planType') = :#{#f.type} or (:#{#f.type} is null)) and " +
            "(p.id = :#{#f.planId} or (:#{#f.planId} is null))) ", nativeQuery = true)
    List<Plan> findPlans(PlanFilterRequest f, Pageable pageable);

    @Query(value = "select case when count(1) > 0 then 'true' else 'false' end from plans p " +
            "where p.id = :planId", nativeQuery = true)
    boolean existsByPlanId(String planId);
}

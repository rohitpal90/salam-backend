package com.salam.dms.repos;

import com.salam.dms.db.entity.DealerPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DealerPlanRepository extends JpaRepository<DealerPlan,Long> {
    List<DealerPlan> findByDealerId(long dealerId);

}

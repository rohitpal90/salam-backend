package com.salam.dms.repos;

import com.salam.dms.db.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    Optional<Request> findByOrderId(String orderId);
    Optional<Request> findByOrderIdAndDealerId(String orderId, Long dealerId);
    Long countByDealerId(Long dealerId);
}

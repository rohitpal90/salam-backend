package com.salam.dms.repos;

import com.salam.dms.db.entity.Dealer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DealerRepository extends JpaRepository<Dealer, Long> {

    @Query("from Dealer d where d.phone = :phone and d.deletedAt is null")
    Optional<Dealer> findActiveDealerByPhone(String phone);
}

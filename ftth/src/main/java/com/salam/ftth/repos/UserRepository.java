package com.salam.ftth.repos;

import com.salam.ftth.db.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = """
            from User u join fetch u.roles 
            where (u.phone = :principal or u.email = :principal) 
            and u.active = :isActive and u.deletedAt is null""")
    Optional<User> findUserByPrincipal(String principal, boolean isActive);

    @Query(value = """
                select
                    r.order_id "orderId",
                    r.state "state",
                    p.meta "planInfo",
                    r.created_at "createdAt"
                from
                    request r
                join `user` u on
                    u.id = r.user_id
                join user_role ur on
                    ur.user_id = r.user_id
                join `role` ro on
                    ro.id = ur.role_id
                JOIN plan p on
                    p.id = JSON_VALUE(r.meta, '$.planInfo.planId')
                where
                    u.id = :userId
                    and ro.`role` = 'CUSTOMER'
            """, nativeQuery = true)
    List<Object []> getCustomerSubscriptions(Long userId, Pageable pageable);
}

package com.salam.ftth.repos;

import com.salam.ftth.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = """
            from User u join fetch u.roles 
            where (u.phone = :principal or u.email = :principal) 
            and u.active = 1 and u.deletedAt is null""")
    Optional<User> findUserByPrincipal(String principal);
}

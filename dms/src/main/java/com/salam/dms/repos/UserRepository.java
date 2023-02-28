package com.salam.dms.repos;

import com.salam.dms.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    @Query(value = " select u.* from user u left join user_role r on r.user_id = u.id where (u.email = :email or u.phone_no = :phone) and u.deleted_at is null",
    nativeQuery = true)
    Optional<User> findByEmail(String email,String phone);
}

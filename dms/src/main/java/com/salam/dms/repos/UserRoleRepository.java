package com.salam.dms.repos;

import com.salam.dms.db.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRoleRepository extends JpaRepository<UserRole,Long> {

       @Query(value = "select * from user_role  where user_id = :userId",nativeQuery = true)
       UserRole findByUserId(long userId);
}

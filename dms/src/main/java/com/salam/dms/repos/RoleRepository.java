package com.salam.dms.repos;

import com.salam.dms.db.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    @Query(value = "select * from role  where id = :roleId",nativeQuery = true)
    Role findByRole(long roleId);
}

package com.logi_flow.backend.repository;

import com.logi_flow.backend.common.enums.user.UserRole;
import com.logi_flow.backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(UserRole userRole);
}

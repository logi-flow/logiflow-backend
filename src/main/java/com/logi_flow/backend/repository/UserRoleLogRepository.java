package com.logi_flow.backend.repository;

import com.logi_flow.backend.entity.UserRoleLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleLogRepository extends JpaRepository<UserRoleLog, Long> {
}

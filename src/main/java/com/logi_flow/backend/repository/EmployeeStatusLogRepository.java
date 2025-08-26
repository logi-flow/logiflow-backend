package com.logi_flow.backend.repository;

import com.logi_flow.backend.entity.EmployeeStatusLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeStatusLogRepository extends JpaRepository<EmployeeStatusLog, Long> {
}

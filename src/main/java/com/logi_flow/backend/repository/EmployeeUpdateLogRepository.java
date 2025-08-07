package com.logi_flow.backend.repository;

import com.logi_flow.backend.entity.EmployeeUpdateLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeUpdateLogRepository extends JpaRepository<EmployeeUpdateLog, Long> {
}

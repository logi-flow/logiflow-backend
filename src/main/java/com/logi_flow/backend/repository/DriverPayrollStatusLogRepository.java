package com.logi_flow.backend.repository;

import com.logi_flow.backend.entity.DriverPayrollStatusLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverPayrollStatusLogRepository extends JpaRepository<DriverPayrollStatusLog, Long> {
}

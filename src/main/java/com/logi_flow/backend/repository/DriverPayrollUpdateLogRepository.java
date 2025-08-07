package com.logi_flow.backend.repository;

import com.logi_flow.backend.entity.DriverPayrollUpdateLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverPayrollUpdateLogRepository extends JpaRepository<DriverPayrollUpdateLog, Long> {
}

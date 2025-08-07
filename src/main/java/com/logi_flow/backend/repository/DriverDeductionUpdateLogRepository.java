package com.logi_flow.backend.repository;

import com.logi_flow.backend.entity.DriverDeductionUpdateLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverDeductionUpdateLogRepository extends JpaRepository<DriverDeductionUpdateLog, Long> {
}

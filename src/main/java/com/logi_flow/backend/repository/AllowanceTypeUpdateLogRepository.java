package com.logi_flow.backend.repository;

import com.logi_flow.backend.entity.AllowanceTypeUpdateLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllowanceTypeUpdateLogRepository extends JpaRepository<AllowanceTypeUpdateLog, Long> {
}

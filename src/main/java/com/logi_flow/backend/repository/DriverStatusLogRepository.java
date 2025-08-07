package com.logi_flow.backend.repository;

import com.logi_flow.backend.entity.DriverStatusLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverStatusLogRepository extends JpaRepository<DriverStatusLog, Long> {
}

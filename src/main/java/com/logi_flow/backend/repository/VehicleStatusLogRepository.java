package com.logi_flow.backend.repository;

import com.logi_flow.backend.entity.VehicleStatusLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleStatusLogRepository extends JpaRepository<VehicleStatusLog, Long> {
}

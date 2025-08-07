package com.logi_flow.backend.repository;

import com.logi_flow.backend.entity.DeliveryStatusLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryStatusLogRepository extends JpaRepository<DeliveryStatusLog, Long> {
}

package com.logi_flow.backend.repository;

import com.logi_flow.backend.entity.DeliveryUpdateLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryUpdateLogRepository extends JpaRepository<DeliveryUpdateLog, Long> {

}

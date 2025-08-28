package com.logi_flow.backend.repository;

import com.logi_flow.backend.common.enums.driver.DriverStatus;
import com.logi_flow.backend.entity.DriverStatusLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface DriverStatusLogRepository extends JpaRepository<DriverStatusLog, Long> {
    long countByNewStatusAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(DriverStatus driverStatus, LocalDateTime localDateTime, LocalDateTime localDateTime1);
}

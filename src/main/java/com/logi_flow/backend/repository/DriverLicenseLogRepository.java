package com.logi_flow.backend.repository;

import com.logi_flow.backend.entity.DriverLicenseLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverLicenseLogRepository extends JpaRepository<DriverLicenseLog, Long> {
}

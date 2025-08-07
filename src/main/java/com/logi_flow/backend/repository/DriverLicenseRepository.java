package com.logi_flow.backend.repository;

import com.logi_flow.backend.entity.DriverLicense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverLicenseRepository extends JpaRepository<DriverLicense, Long> {
}

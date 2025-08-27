package com.logi_flow.backend.repository;

import com.logi_flow.backend.entity.DriverLicense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DriverLicenseRepository extends JpaRepository<DriverLicense, Long> {
    List<DriverLicense> findByExpiredDateBetween(LocalDate today, LocalDate oneMonthLater);
}

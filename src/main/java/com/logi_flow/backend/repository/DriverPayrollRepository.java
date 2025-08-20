package com.logi_flow.backend.repository;

import com.logi_flow.backend.common.enums.DriverPayrollStatus;
import com.logi_flow.backend.entity.DriverPayroll;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DriverPayrollRepository extends JpaRepository<DriverPayroll, Long> {
    Optional<DriverPayroll> findByDriverIdAndPeriodStartDateAndPeriodEndDateAndStatus(Long driverId, LocalDate periodStartDate, LocalDate periodEndDate, DriverPayrollStatus driverPayrollStatus);
    Page<DriverPayroll> findByDriverIdAndStatus(Long driver_id, DriverPayrollStatus status, Pageable pageable);
}

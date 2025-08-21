package com.logi_flow.backend.repository;

import com.logi_flow.backend.common.enums.DriverPayrollStatus;
import com.logi_flow.backend.entity.DriverPayroll;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DriverPayrollRepository extends JpaRepository<DriverPayroll, Long> {
    Optional<DriverPayroll> findByDriverIdAndPeriodStartDateAndPeriodEndDateAndStatus(Long driverId, LocalDate periodStartDate, LocalDate periodEndDate, DriverPayrollStatus driverPayrollStatus);
    Page<DriverPayroll> findByDriverIdAndStatus(Long driver_id, DriverPayrollStatus status, Pageable pageable);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        select p.id from DriverPayroll p
        where p.driver.id = :driverId
            and p.status in ('CREATED', 'CONFIRMED')
            and p.periodStartDate <= :endDate
            and p.periodEndDate >= :startDate
    """)
    Optional<Long> lockAnyActiveOverlapId(@Param("driverId") Long driverId,
                                          @Param("startDate") LocalDate startDate,
                                          @Param("endDate") LocalDate endDate);
}

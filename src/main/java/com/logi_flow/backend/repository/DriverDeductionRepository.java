package com.logi_flow.backend.repository;

import com.logi_flow.backend.common.enums.DriverDeductionStatus;
import com.logi_flow.backend.common.enums.DriverPayrollStatus;
import com.logi_flow.backend.entity.DriverDeduction;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface DriverDeductionRepository extends JpaRepository<DriverDeduction, Long> {
    Optional<DriverDeduction> findByDriverPayrollIdAndDeductionTypeIdAndStatus(Long payrollId, Long deductionTypeId, DriverDeductionStatus driverDeductionStatus);
    List<DriverDeduction> findByDriverPayrollIdAndStatus(Long payrollId, DriverDeductionStatus driverDeductionStatus);
    List<DriverDeduction> findAllByIdInAndDriverPayrollId(Set<Long> ids, Long payrollId);
    boolean existsByDeductionTypeIdAndDriverPayroll_Status(Long deductionTypeId, DriverPayrollStatus driverPayrollStatus);

    @Query("SELECT COALESCE(SUM(d.amount), 0) FROM DriverDeduction d WHERE d.driverPayroll.id = :driverPayrollId AND d.status <> 'DELETED'")
    int sumAmountByDriverPayrollId(@Param("driverPayrollId") Long driverPayrollId);

}

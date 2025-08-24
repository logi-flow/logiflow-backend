package com.logi_flow.backend.repository;

import com.logi_flow.backend.common.enums.DriverAllowanceStatus;
import com.logi_flow.backend.common.enums.DriverPayrollStatus;
import com.logi_flow.backend.entity.DriverAllowance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface DriverAllowanceRepository extends JpaRepository<DriverAllowance, Long> {
    Optional<DriverAllowance> findByDriverPayrollIdAndAllowanceTypeIdAndStatus(Long payrollId, Long allowanceTypeId, DriverAllowanceStatus driverAllowanceStatus);
    List<DriverAllowance> findByDriverPayrollIdAndStatus(Long payrollId, DriverAllowanceStatus driverAllowanceStatus);
    List<DriverAllowance> findAllByIdInAndDriverPayrollId(Collection<Long> ids, Long payrollId);
    boolean existsByAllowanceTypeIdAndDriverPayroll_Status(Long allowanceTypeId, DriverPayrollStatus driverPayrollStatus);

    @Query("SELECT COALESCE(SUM(a.amount), 0) FROM DriverAllowance a WHERE a.driverPayroll.id = :driverPayrollId AND a.status <> 'DELETED'")
    int sumAmountByDriverPayrollId(@Param("driverPayrollId") Long driverPayrollId);

}

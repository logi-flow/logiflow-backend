package com.logi_flow.backend.repository;

import com.logi_flow.backend.entity.DriverAllowance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface DriverAllowanceRepository extends JpaRepository<DriverAllowance, Long> {
    Optional<DriverAllowance> findByDriverPayrollId(Long payrollId);
    List<DriverAllowance> findAllByIdInAndDriverPayrollId(Collection<Long> ids, Long payrollId);
}

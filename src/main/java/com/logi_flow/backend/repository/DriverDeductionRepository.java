package com.logi_flow.backend.repository;

import com.logi_flow.backend.entity.DriverAllowance;
import com.logi_flow.backend.entity.DriverDeduction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface DriverDeductionRepository extends JpaRepository<DriverDeduction, Long> {
    Optional<DriverDeduction> findByDriverPayrollId(Long payrollId);
    List<DriverDeduction> findAllByIdInAndDriverPayrollId(Set<Long> ids, Long payrollId);
}

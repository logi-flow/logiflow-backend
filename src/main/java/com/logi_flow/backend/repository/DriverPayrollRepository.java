package com.logi_flow.backend.repository;

import com.logi_flow.backend.entity.DriverPayroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverPayrollRepository extends JpaRepository<DriverPayroll, Long> {
}

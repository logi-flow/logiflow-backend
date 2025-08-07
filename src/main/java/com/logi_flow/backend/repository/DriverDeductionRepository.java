package com.logi_flow.backend.repository;

import com.logi_flow.backend.entity.DriverDeduction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverDeductionRepository extends JpaRepository<DriverDeduction, Long> {
}

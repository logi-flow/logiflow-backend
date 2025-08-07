package com.logi_flow.backend.repository;

import com.logi_flow.backend.entity.EmployeeOrgLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeOrgLogRepository extends JpaRepository<EmployeeOrgLog, Long> {
}

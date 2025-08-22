package com.logi_flow.backend.repository;

import com.logi_flow.backend.entity.AssignmentStatusLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentStatusLogRepository extends JpaRepository<AssignmentStatusLog, Long> {
}

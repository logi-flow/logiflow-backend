package com.logi_flow.backend.repository;

import com.logi_flow.backend.entity.DeleteLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeleteLogRepository extends JpaRepository<DeleteLog, Long> {
}

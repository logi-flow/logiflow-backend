package com.logi_flow.backend.repository;

import com.logi_flow.backend.entity.AllocationUpdateLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllocationUpdateLogRepository extends JpaRepository<AllocationUpdateLog, Long> {

}

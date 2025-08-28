package com.logi_flow.backend.repository;

import com.logi_flow.backend.entity.Allocation;
import com.logi_flow.backend.entity.AllocationStatusLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AllocationStatusLogRepository extends JpaRepository<AllocationStatusLog, Long> {

    Optional<AllocationStatusLog> findByAllocation(Allocation allocation);
}

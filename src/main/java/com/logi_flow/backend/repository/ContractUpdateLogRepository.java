package com.logi_flow.backend.repository;

import com.logi_flow.backend.entity.ContractUpdateLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractUpdateLogRepository extends JpaRepository<ContractUpdateLog, Long> {
}

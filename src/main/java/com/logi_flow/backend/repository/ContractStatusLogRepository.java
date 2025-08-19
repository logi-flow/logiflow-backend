package com.logi_flow.backend.repository;

import com.logi_flow.backend.entity.ContractStatusLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractStatusLogRepository extends JpaRepository<ContractStatusLog, Long> {
}

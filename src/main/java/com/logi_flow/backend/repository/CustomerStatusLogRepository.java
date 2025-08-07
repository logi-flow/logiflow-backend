package com.logi_flow.backend.repository;

import com.logi_flow.backend.entity.CustomerStatusLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerStatusLogRepository extends JpaRepository<CustomerStatusLog, Long> {
}

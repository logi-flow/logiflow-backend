package com.logi_flow.backend.repository;

import com.logi_flow.backend.entity.Allocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AllocationRepository extends JpaRepository<Allocation, Long> {
    Optional<Allocation> findByDeliveryId(Long id);
}

package com.logi_flow.backend.repository;

import com.logi_flow.backend.entity.Contract;
import com.logi_flow.backend.entity.Delivery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Page<Delivery> findByCustomerId(Long id, Pageable pageable);

    @Query("""
    SELECT d
    FROM Delivery d
    LEFT JOIN Allocation a ON a.delivery = d
    WHERE a.id IS NULL
    """)
    Page<Delivery> findAllWaitingDelivery(Pageable pageable);

    @Query("""
        SELECT COUNT(d)
        FROM Delivery d
        WHERE d.contract = :contract
        AND d.createdAt >= :startDate
        AND d.createdAt <= :endDate
    """)
    long countByContractAndCreatedAtBetween(
            @Param("contract") Contract contract,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
            );
}

package com.logi_flow.backend.repository;

import com.logi_flow.backend.entity.Customer;
import com.logi_flow.backend.entity.ReturnDelivery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnDeliveryRepository extends JpaRepository<ReturnDelivery, Long> {
    boolean existsByDeliveryId(Long id);

    @Query("""
        SELECT r
        FROM ReturnDelivery r
        LEFT JOIN Allocation a ON a.returnDelivery = r
        WHERE a.id IS NULL AND r.status = "ASSIGNED"
        """)
    Page<ReturnDelivery> findAllWaitingReturnDelivery(Pageable pageable);

    Page<ReturnDelivery> findByDeliveryCustomerAndIsHiddenFalse(Customer customer, Pageable pageable);
}

package com.logi_flow.backend.repository;

import com.logi_flow.backend.entity.Customer;
import com.logi_flow.backend.entity.ReturnDelivery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnDeliveryRepository extends JpaRepository<ReturnDelivery, Long> {
    boolean existsByDeliveryId(Long id);

    Page<ReturnDelivery> findByDeliveryCustomer(Customer customer, Pageable pageable);
}

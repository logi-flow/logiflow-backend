package com.logi_flow.backend.repository;

import com.logi_flow.backend.entity.Customer;
import com.logi_flow.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByBusinessNumber(String businessNumber);
    Optional<Customer> findByUser(User user);
}

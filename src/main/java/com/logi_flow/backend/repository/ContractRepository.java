package com.logi_flow.backend.repository;

import com.logi_flow.backend.common.enums.ContractStatus;
import com.logi_flow.backend.entity.Contract;
import com.logi_flow.backend.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    Optional<Contract> findByCustomerAndStatus(Customer customer, ContractStatus contractStatus);

    Page<Contract> findByCustomerId(Long id, Pageable pageable);
}

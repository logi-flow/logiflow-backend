package com.logi_flow.backend.repository;

import com.logi_flow.backend.common.enums.AllowanceTypeStatus;
import com.logi_flow.backend.entity.AllowanceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AllowanceTypeRepository extends JpaRepository<AllowanceType, Long> {
    Optional<AllowanceType> findByCode(String code);
    Optional<AllowanceType> findByCodeAndStatus(String code, AllowanceTypeStatus status);
    Page<AllowanceType> findByStatus(AllowanceTypeStatus status, Pageable pageable);
}

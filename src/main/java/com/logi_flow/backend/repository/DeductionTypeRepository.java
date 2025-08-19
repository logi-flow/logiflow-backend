package com.logi_flow.backend.repository;

import com.logi_flow.backend.common.enums.DeductionTypeStatus;
import com.logi_flow.backend.entity.DeductionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeductionTypeRepository extends JpaRepository<DeductionType, Long> {
    Optional<DeductionType> findByCodeAndStatus(String code, DeductionTypeStatus deductionTypeStatus);
    Page<DeductionType> findByStatus(DeductionTypeStatus deductionTypeStatus, Pageable pageable);
}

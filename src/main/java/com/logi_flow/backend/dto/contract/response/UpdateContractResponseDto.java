package com.logi_flow.backend.dto.contract.response;

import com.logi_flow.backend.common.enums.ContractStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class UpdateContractResponseDto {
    private Long id;
    private Long customerId;
    private ContractStatus status;
    private LocalDate stratDate;
    private LocalDate endDate;
    private int price;
    private int volumeLimit;
    private String specialTerms;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

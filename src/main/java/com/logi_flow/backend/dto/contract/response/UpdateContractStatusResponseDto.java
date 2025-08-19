package com.logi_flow.backend.dto.contract.response;

import com.logi_flow.backend.common.enums.ContractStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class UpdateContractStatusResponseDto {
    private Long id;
    private Long customerId;
    private ContractStatus status;
    private Long changedBy;
    private String changedByUsername;
    private String changedReason;
    private ContractStatus prevStatus;
    private ContractStatus newStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

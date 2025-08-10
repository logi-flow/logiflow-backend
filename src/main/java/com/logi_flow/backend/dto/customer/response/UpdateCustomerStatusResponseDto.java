package com.logi_flow.backend.dto.customer.response;

import com.logi_flow.backend.common.enums.ContractStatus;
import com.logi_flow.backend.common.enums.CustomerStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class UpdateCustomerStatusResponseDto {
    private Long id;
    private CustomerStatus status;
    private Long changedBy;
    private String changedByUsername;
    private String changedReason;
    private String prevStatus;
    private String newStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

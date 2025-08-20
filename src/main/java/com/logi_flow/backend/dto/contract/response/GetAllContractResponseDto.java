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
public class GetAllContractResponseDto {
    private Long id;
    private Long customerId;
    private String customerName;
    private ContractStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private String createdAt;
    private String updatedAt;
}

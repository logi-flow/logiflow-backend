package com.logi_flow.backend.dto.contractLog.response;

import com.logi_flow.backend.common.enums.ContractStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class GetContractStatusLogResponseDto {
    private String customerName;
    private String businessNumber;
    private String representativeName;
    private String changedByUsername;
    private String changeReason;
    private ContractStatus prevStatus;
    private ContractStatus newStatus;
    private String createdAt;
}

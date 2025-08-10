package com.logi_flow.backend.dto.contract.request;

import com.logi_flow.backend.common.enums.ContractStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateContractStatusRequestDto {
    private ContractStatus status;
    private String changedReason;
}

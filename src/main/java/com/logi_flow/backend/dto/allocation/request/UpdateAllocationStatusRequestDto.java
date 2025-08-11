package com.logi_flow.backend.dto.allocation.request;

import com.logi_flow.backend.common.enums.AllocationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateAllocationStatusRequestDto {
    private AllocationStatus status;
    private String changeReason;
}

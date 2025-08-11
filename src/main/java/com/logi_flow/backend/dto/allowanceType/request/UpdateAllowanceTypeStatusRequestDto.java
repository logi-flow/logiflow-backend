package com.logi_flow.backend.dto.allowanceType.request;

import com.logi_flow.backend.common.enums.AllowanceTypeStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateAllowanceTypeStatusRequestDto {
    private AllowanceTypeStatus status;
    private String changeReason;
}

package com.logi_flow.backend.dto.deductionType.request;

import com.logi_flow.backend.common.enums.DeductionTypeStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateDeductionTypeStatusRequestDto {
    private DeductionTypeStatus status;
    private String changeReason;
}

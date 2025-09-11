package com.logi_flow.backend.dto.deductionType.response;

import com.logi_flow.backend.common.enums.DeductionTypeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class CreateDeductionTypeResponseDto {
    private Long id;
    private String code;
    private String name;
    private String description;
    private boolean isActive;
    private DeductionTypeStatus status;

    private String createdAt;
    private String updatedAt;
}

package com.logi_flow.backend.dto.deductionType.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class GetDeductionTypeDetailResponseDto {
    private String code;
    private String name;
    private String description;
    private boolean isActive;

    private String createdAt;
    private String updatedAt;
}

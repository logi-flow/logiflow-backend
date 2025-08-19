package com.logi_flow.backend.dto.allowanceType.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class GetAllAllowanceTypeResponseDto {
    private String code;
    private String name;
    private boolean isActive;

    private String createdAt;
    private String updatedAt;
}

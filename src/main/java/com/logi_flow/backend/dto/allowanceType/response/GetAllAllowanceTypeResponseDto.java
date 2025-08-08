package com.logi_flow.backend.dto.allowanceType.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class GetAllAllowanceTypeResponseDto {
    private String code;
    private String name;
    private boolean isActive;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

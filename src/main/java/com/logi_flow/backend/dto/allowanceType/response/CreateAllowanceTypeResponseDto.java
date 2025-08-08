package com.logi_flow.backend.dto.allowanceType.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class CreateAllowanceTypeResponseDto {
    private Long id;
    private String code;
    private String name;
    private String description;
    private boolean isActive;
    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

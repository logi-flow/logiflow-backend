package com.logi_flow.backend.dto.deductionType.response;

import com.logi_flow.backend.common.enums.DeductionTypeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class UpdateDeductionTypeStatusResponseDto {
    private Long id;
    private String code;
    private String name;
    private String description;
    private boolean isActive;
    private DeductionTypeStatus status;
    private String changeReason;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

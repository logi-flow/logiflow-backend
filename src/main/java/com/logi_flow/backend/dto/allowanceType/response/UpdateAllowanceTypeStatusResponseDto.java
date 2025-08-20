package com.logi_flow.backend.dto.allowanceType.response;

import com.logi_flow.backend.common.enums.AllowanceTypeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class UpdateAllowanceTypeStatusResponseDto {
    private Long id;
    private String code;
    private String name;
    private String description;
    private boolean isActive;
    private AllowanceTypeStatus status;
    private String changeReason;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

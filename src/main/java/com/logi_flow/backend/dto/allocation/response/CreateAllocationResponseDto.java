package com.logi_flow.backend.dto.allocation.response;

import com.logi_flow.backend.common.enums.AllocationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class CreateAllocationResponseDto {
    private Long id;
    private Long deliveryId;
    private Long assignmentId;
    private String districtName;
    private AllocationStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

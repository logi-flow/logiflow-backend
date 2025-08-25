package com.logi_flow.backend.dto.assignment.response;

import com.logi_flow.backend.common.enums.AssignmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class GetAllAssignmentResponseDto {
    private Long id;
    private Long driverId;
    private Long vehicleId;
    private boolean isPrimary;
    private AssignmentStatus status;
    private String createdAt;
    private String updatedAt;
}

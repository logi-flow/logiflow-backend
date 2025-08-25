package com.logi_flow.backend.dto.assignment.response;

import com.logi_flow.backend.common.enums.AssignmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class CreateAssignmentResponseDto {
    private Long id;
    private Long driverId;
    private String driverName;
    private Long vehicleIds;
    private Boolean isPrimary;
    private AssignmentStatus status;
    private String createdAt;
    private String updatedAt;
}

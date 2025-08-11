package com.logi_flow.backend.dto.assignment.response;

import com.logi_flow.backend.common.enums.AssignmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class CreateAssignmentResponseDto {
    private Long id;
    private Long driverId;
    private List<Long> vehicleIds;
    private Boolean isPrimary;
    private AssignmentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

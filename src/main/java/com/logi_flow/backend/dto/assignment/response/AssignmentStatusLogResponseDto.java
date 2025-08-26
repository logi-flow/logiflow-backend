package com.logi_flow.backend.dto.assignment.response;

import com.logi_flow.backend.common.enums.AssignmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class AssignmentStatusLogResponseDto {
    private Long id;
    private Long assignmentId;
    private String changedByUsername;
    private String changeReason;
    private AssignmentStatus prevStatus;
    private AssignmentStatus newStatus;
    private String createdAt;
}
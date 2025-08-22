package com.logi_flow.backend.dto.assignment.request;

import com.logi_flow.backend.common.enums.AssignmentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateAssignmentStatusRequestDto {
    private AssignmentStatus status;
    private String changeReason;
}
package com.logi_flow.backend.dto.assignment.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class AssignmentUpdateLogResponseDto {
    private Long id;
    private Long AssignmentId;
    private String changedByUsername;
    private String type;
    private String prevData;
    private String newData;
    private String createdAt;
}

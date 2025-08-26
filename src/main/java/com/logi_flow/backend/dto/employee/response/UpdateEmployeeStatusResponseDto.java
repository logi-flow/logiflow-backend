package com.logi_flow.backend.dto.employee.response;

import com.logi_flow.backend.common.enums.employee.EmployeeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class UpdateEmployeeStatusResponseDto {
    private Long id;
    private EmployeeStatus status;
    private Long changedBy;
    private String changedByUsername;
    private String changedReason;
    private EmployeeStatus prevStatus;
    private EmployeeStatus newStatus;
    private String createdAt;
    private String updatedAt;
}

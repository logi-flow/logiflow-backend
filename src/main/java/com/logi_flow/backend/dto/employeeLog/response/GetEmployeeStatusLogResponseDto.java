package com.logi_flow.backend.dto.employeeLog.response;

import com.logi_flow.backend.common.enums.employee.EmployeeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class GetEmployeeStatusLogResponseDto {
    private Long id;
    private Long employeeId;
    private String username;
    private String changeReason;
    private EmployeeStatus prevStatus;
    private EmployeeStatus newStatus;
    private String createdAt;
}

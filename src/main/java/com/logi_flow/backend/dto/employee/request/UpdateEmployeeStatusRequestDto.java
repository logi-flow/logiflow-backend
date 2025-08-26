package com.logi_flow.backend.dto.employee.request;

import com.logi_flow.backend.common.enums.employee.EmployeeStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateEmployeeStatusRequestDto {
    private EmployeeStatus status;
    private String changedReason;
}

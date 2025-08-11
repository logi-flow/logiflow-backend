package com.logi_flow.backend.dto.driverPayroll.request;

import com.logi_flow.backend.common.enums.DriverPayrollStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateDriverPayrollStatusRequestDto {
    private DriverPayrollStatus status;
    private String changeReason;
}

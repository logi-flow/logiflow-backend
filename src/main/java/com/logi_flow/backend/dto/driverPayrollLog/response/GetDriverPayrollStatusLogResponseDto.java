package com.logi_flow.backend.dto.driverPayrollLog.response;

import com.logi_flow.backend.common.enums.DriverPayrollStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class GetDriverPayrollStatusLogResponseDto {
    private Long id;
    private Long driverId;
    private String driverName;
    private Long payrollId;
    private DriverPayrollStatus prevStatus;
    private DriverPayrollStatus newStatus;
    private String changedByUsername;
    private String changeReason;

    private String createdAt;
}

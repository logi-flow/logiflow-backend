package com.logi_flow.backend.dto.driverPayrollLog.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class GetDriverPayrollUpdateLogResponseDto {
    private Long id;
    private Long driverId;
    private String driverName;
    private Long payrollId;
    private String type;
    private String prevData;
    private String newData;
    private String changedByUsername;

    private String createdAt;
}

package com.logi_flow.backend.dto.driverPayroll.response;

import com.logi_flow.backend.common.enums.DriverPayrollStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class GetAllDriverPayrollResponseDto {
    private Long id;
    private Long driverId;
    private String driverName;
    private String title;
    private int totalAllowance;
    private int totalDeduction;
    private int finalAmount;
    private DriverPayrollStatus status;

    private String createdAt;
    private String updatedAt;
}

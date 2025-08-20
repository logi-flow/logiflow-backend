package com.logi_flow.backend.dto.driverPayroll.response;

import com.logi_flow.backend.common.enums.DriverPayrollStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Builder
public class UpdateDriverPayrollStatusResponseDto {
    private Long id;
    private Long driverId;
    private String title;
    private LocalDate periodStartDate;
    private LocalDate periodEndDate;
    private int totalAllowance;
    private int totalDeduction;
    private int finalAmount;
    private DriverPayrollStatus status;
    private String changeReason;

    private String createdAt;
    private String updatedAt;
}

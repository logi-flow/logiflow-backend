package com.logi_flow.backend.dto.driverPayroll.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Builder
public class GetDriverPayrollDetailResponseDto {
    private Long driverId;
    private String driverName;
    private String title;
    private LocalDate periodStartDate;
    private LocalDate periodEndDate;
    private int totalAllowance;
    private int totalDeduction;
    private int finalAmount;
    private String status;

    private String createdAt;
    private String updatedAt;
}

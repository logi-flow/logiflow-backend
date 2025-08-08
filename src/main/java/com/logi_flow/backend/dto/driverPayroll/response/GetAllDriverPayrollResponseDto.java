package com.logi_flow.backend.dto.driverPayroll.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class GetAllDriverPayrollResponseDto {
    private Long driverId;
    private String driverName;
    private String title;
    private int totalAllowance;
    private int totalDeduction;
    private int finalAmount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

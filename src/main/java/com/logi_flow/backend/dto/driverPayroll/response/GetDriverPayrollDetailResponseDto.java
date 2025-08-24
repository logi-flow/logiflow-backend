package com.logi_flow.backend.dto.driverPayroll.response;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
public class GetDriverPayrollDetailResponseDto {
    private Long id;
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

    @Builder.Default
    private List<AllowanceItemDto> allowanceItems = new ArrayList<>();

    @Builder.Default
    private List<DeductionItemDto> deductionItems = new ArrayList<>();
}

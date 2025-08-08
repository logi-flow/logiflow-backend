package com.logi_flow.backend.dto.driverPayroll.request;

import com.logi_flow.backend.common.enums.DriverPayrollStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class UpdateDriverPayrollRequestDto {
    private String title;

    @NotNull(message = "시작 일자는 필수 항목입니다.")
    private LocalDate periodStartDate;

    @NotNull(message = "종료 일자는 필수 항목입니다.")
    private LocalDate periodEndDate;

    private DriverPayrollStatus status;
}

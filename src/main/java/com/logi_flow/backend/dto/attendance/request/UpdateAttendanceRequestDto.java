package com.logi_flow.backend.dto.attendance.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Builder
public class UpdateAttendanceRequestDto {
    @NotNull(message = "차량 주행거리는 필수 항목입니다.")
    private BigDecimal vehicleMileage;
}

package com.logi_flow.backend.dto.attendance.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Builder
public class UpdateAttendanceResponseDto {
    private Long id;
    private Long driverId;
    private String workStart;
    private String workEnd;
    private Integer openFlag;
    private BigDecimal vehicleMileage;

    private String createdAt;
    private String updatedAt;
}

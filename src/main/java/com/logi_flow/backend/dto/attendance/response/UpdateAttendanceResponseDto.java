package com.logi_flow.backend.dto.attendance.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class UpdateAttendanceResponseDto {
    private Long id;
    private Long driverId;
    private Long employeeId;
    private LocalDateTime workStart;
    private LocalDateTime workEnd;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

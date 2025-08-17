package com.logi_flow.backend.dto.attendance.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class CreateAttendanceResponseDto {
    private Long id;
    private Long driverId;
    private String workStart;
    private Integer openFlag;

    private String createdAt;
    private String updatedAt;
}

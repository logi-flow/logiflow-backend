package com.logi_flow.backend.dto.attendance.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class GetAllMyAttendanceResponseDto {
    private Long id;
    private String workStart;
    private String workEnd;

    private String createdAt;
    private String updatedAt;
}

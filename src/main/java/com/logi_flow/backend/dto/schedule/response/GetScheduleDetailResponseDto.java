package com.logi_flow.backend.dto.schedule.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class GetScheduleDetailResponseDto {
    private Long id;
    private Long allocationId;
    private LocalDate allocationDate;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

package com.logi_flow.backend.dto.schedule.request;

import com.logi_flow.backend.common.enums.AllocationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class UpdateScheduleRequestDto {
    private AllocationStatus status;
    private String changeReason;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
}

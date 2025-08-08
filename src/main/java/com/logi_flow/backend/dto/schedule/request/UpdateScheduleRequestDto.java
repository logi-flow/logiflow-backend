package com.logi_flow.backend.dto.schedule.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class UpdateScheduleRequestDto {
    private LocalDate allocationDate;
    private LocalDateTime departureDate;
    private LocalDateTime arrivalDate;
}

package com.logi_flow.backend.dto.schedule.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class UpdateScheduleRequestDto {
    @NotNull(message = "배차는 필수 항목입니다.")
    private Long allocationId;
    @NotNull(message = "배차 날짜는 필수 항목입니다.")
    private LocalDate allocationDate;
    private LocalDateTime departureDate;
    private LocalDateTime arrivalDate;
}

package com.logi_flow.backend.dto.driver.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class UpdateDriverResponseDto {
    private Long driverId;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

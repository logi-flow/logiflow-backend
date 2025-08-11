package com.logi_flow.backend.dto.employee.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class CreateEmployeeResponseDto {
    private Long id;
    private String username;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

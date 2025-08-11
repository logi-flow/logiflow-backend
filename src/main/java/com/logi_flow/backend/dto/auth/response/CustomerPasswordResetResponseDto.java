package com.logi_flow.backend.dto.auth.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class CustomerPasswordResetResponseDto {
    private Long userId;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

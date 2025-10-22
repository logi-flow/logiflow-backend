package com.logi_flow.backend.dto.alert.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AlertResponseDto {
    private Long id;
    private String message;
    private LocalDateTime createdAt;
    private boolean isRead;
}

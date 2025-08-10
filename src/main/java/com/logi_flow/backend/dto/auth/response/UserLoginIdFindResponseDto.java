package com.logi_flow.backend.dto.auth.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class UserLoginIdFindResponseDto {
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

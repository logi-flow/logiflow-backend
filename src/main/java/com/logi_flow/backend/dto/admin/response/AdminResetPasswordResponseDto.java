package com.logi_flow.backend.dto.admin.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class AdminResetPasswordResponseDto {
    private Long userId;
    private String email;
    private String createdAt;
    private String updatedAt;
}

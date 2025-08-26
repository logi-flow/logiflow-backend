package com.logi_flow.backend.dto.auth.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class FirstPasswordChangeResponseDto {
    private String username;
    private String createdAt;
    private String updatedAt;
}

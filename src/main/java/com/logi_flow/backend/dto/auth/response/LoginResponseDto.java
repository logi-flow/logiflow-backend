package com.logi_flow.backend.dto.auth.response;

import com.logi_flow.backend.common.enums.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class LoginResponseDto {
    private String token;
    private long exprTime;
    private Long id;
    private UserRole role;
    private String username;
    private String name;
    private String profileImageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

package com.logi_flow.backend.dto.user.response;

import com.logi_flow.backend.common.enums.user.UserRole;
import com.logi_flow.backend.common.enums.user.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class GetUserDetailResponseDto {
    private Long id;
    private String username;
    private UserRole role;
    private UserStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

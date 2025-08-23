package com.logi_flow.backend.dto.user.response;

import com.logi_flow.backend.common.enums.CustomerStatus;
import com.logi_flow.backend.common.enums.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class UpdateUserRoleResponseDto {
    private Long id;
    private UserRole role;
    private Long changedBy;
    private String changedByUsername;
    private String changedReason;
    private UserRole prevRole;
    private UserRole newRole;
    private String createdAt;
    private String updatedAt;
}

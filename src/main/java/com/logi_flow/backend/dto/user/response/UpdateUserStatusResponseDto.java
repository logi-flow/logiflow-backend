package com.logi_flow.backend.dto.user.response;

import com.logi_flow.backend.common.enums.CustomerStatus;
import com.logi_flow.backend.common.enums.user.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class UpdateUserStatusResponseDto {
    private Long id;
    private UserStatus status;
    private Long changedBy;
    private String changedByUsername;
    private String changedReason;
    private String prevStatus;
    private String newStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

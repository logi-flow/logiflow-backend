package com.logi_flow.backend.dto.userLog.response;

import com.logi_flow.backend.common.enums.user.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class GetUserStatusLogResponseDto {
    private Long id;
    private Long userId;
    private String username;
    private String changeReason;
    private UserStatus prevStatus;
    private UserStatus newStatus;
    private String createdAt;
}

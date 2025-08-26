package com.logi_flow.backend.dto.userLog.response;

import com.logi_flow.backend.common.enums.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class GetUserRoleLogResponseDto {
    private Long id;
    private Long userId;
    private String username;
    private String changeReason;
    private UserRole prevRole;
    private UserRole newRole;
    private String createdAt;
}

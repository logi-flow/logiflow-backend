package com.logi_flow.backend.dto.user.response;

import com.logi_flow.backend.common.enums.user.UserRole;
import com.logi_flow.backend.common.enums.user.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class GetAllUserResponseDto {
    private Long id;
    private String username;
    private UserRole role;
    private UserStatus status;
}

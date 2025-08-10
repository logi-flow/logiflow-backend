package com.logi_flow.backend.dto.user.request;

import com.logi_flow.backend.common.enums.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateUserRoleRequestDto {
    private UserRole role;
    private String changedReason;
}

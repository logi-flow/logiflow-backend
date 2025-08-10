package com.logi_flow.backend.dto.user.request;

import com.logi_flow.backend.common.enums.user.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateUserStatusRequestDto {
    private UserStatus status;
    private String changedReason;
}

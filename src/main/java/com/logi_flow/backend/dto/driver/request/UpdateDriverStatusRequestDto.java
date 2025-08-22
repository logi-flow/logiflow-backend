package com.logi_flow.backend.dto.driver.request;

import com.logi_flow.backend.common.enums.driver.DriverStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateDriverStatusRequestDto {
    private DriverStatus status;
    private String changeReason;
}

package com.logi_flow.backend.dto.driver.response;

import com.logi_flow.backend.common.enums.driver.DriverStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class DriverStatusLogResponseDto {
    private Long id;
    private Long driverId;
    private String changedByUsername;
    private String changeReason;
    private DriverStatus prevStatus;
    private DriverStatus newStatus;
    private String createdAt;
}

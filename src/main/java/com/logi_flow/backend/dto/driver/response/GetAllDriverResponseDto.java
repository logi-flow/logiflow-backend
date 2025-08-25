package com.logi_flow.backend.dto.driver.response;

import com.logi_flow.backend.common.enums.driver.DriverStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class GetAllDriverResponseDto {
    private Long driverId;
    private String name;
    private DriverStatus status;
    private String phoneNumber;
    private String createdAt;
    private String updatedAt;
}

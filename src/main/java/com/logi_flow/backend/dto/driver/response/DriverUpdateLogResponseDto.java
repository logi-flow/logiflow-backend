package com.logi_flow.backend.dto.driver.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class DriverUpdateLogResponseDto {
    private Long id;
    private Long driverId;
    private String changedByUsername;
    private String type;
    private String prevData;
    private String newData;
    private String createdAt;
}

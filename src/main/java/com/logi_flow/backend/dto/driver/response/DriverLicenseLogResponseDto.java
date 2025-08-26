package com.logi_flow.backend.dto.driver.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class DriverLicenseLogResponseDto {
    private Long id;
    private Long licenseId;
    private String driverName;
    private String changedByUsername;
    private String type;
    private String prevData;
    private String newData;
    private String createdAt;
}

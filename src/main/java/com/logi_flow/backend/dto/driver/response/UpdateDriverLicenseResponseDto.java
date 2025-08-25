package com.logi_flow.backend.dto.driver.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Builder
public class UpdateDriverLicenseResponseDto {
    private Long driverLicenseId;
    private String name;
    private String driverNumber;
    private LocalDate expiredDate;
    private String createdAt;
    private String updatedAt;
}

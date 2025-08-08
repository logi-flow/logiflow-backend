package com.logi_flow.backend.dto.driver.request;

import com.logi_flow.backend.common.enums.driver.DriverLicenseType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class UpdateDriverLicenseRequestDto {
    private DriverLicenseType type;
    private LocalDate expiredDate;
}

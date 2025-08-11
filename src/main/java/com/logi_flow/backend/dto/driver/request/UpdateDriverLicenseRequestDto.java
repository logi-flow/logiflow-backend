package com.logi_flow.backend.dto.driver.request;

import com.logi_flow.backend.common.enums.driver.DriverLicenseType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class UpdateDriverLicenseRequestDto {
    @NotNull(message = "자격증 종류는 필수 항목입니다.")
    private DriverLicenseType type;

    @NotNull(message = "만료일은 필수 항목입니다.")
    private LocalDate expiredDate;
}

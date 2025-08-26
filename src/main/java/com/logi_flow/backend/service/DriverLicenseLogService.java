package com.logi_flow.backend.service;

import com.logi_flow.backend.dto.driver.response.DriverLicenseLogResponseDto;
import org.springframework.data.domain.Page;

public interface DriverLicenseLogService {
    Page<DriverLicenseLogResponseDto> getUpdateLog(int page, int size, String sort);
}

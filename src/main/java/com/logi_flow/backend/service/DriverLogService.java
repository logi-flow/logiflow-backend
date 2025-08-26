package com.logi_flow.backend.service;

import com.logi_flow.backend.dto.driver.response.DriverStatusLogResponseDto;
import com.logi_flow.backend.dto.driver.response.DriverUpdateLogResponseDto;
import org.springframework.data.domain.Page;

public interface DriverLogService {
    Page<DriverStatusLogResponseDto> getStatusLog(int page, int size, String sort);

    Page<DriverUpdateLogResponseDto> getUpdateLog(int page, int size, String sort);
}

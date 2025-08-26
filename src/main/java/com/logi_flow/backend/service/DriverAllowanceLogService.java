package com.logi_flow.backend.service;

import com.logi_flow.backend.dto.driverAllowanceLog.response.GetDriverAllowanceUpdateLogResponseDto;
import org.springframework.data.domain.Page;

public interface DriverAllowanceLogService {
    Page<GetDriverAllowanceUpdateLogResponseDto> getDriverAllowanceUpdateLogs(int page, int size, String sort);
}

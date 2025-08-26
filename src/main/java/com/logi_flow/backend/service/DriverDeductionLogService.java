package com.logi_flow.backend.service;

import com.logi_flow.backend.dto.driverDeductionLog.response.GetDriverDeductionUpdateLogResponseDto;
import org.springframework.data.domain.Page;

public interface DriverDeductionLogService {
    Page<GetDriverDeductionUpdateLogResponseDto> getDriverDeductionUpdateLogs(int page, int size, String sort);
}

package com.logi_flow.backend.service;

import com.logi_flow.backend.dto.driverPayrollLog.response.GetDriverPayrollStatusLogResponseDto;
import com.logi_flow.backend.dto.driverPayrollLog.response.GetDriverPayrollUpdateLogResponseDto;
import org.springframework.data.domain.Page;

public interface DriverPayrollLogService {
    Page<GetDriverPayrollUpdateLogResponseDto> getDriverPayrollUpdateLogs(int page, int size, String sort);
    Page<GetDriverPayrollStatusLogResponseDto> getDriverPayrollStatusLogs(int page, int size, String sort);
}

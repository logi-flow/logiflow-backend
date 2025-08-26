package com.logi_flow.backend.service;

import com.logi_flow.backend.dto.employeeLog.response.GetEmployeeStatusLogResponseDto;
import com.logi_flow.backend.dto.employeeLog.response.GetEmployeeUpdateLogResponseDto;
import org.springframework.data.domain.Page;

public interface EmployeeLogService {
    Page<GetEmployeeUpdateLogResponseDto> getEmployeeUpdateLogs(int page, int size, String sort);

    Page<GetEmployeeStatusLogResponseDto> getEmployeeStatusLogs(int page, int size, String sort);
}

package com.logi_flow.backend.service;

import com.logi_flow.backend.dto.allowanceTypeLog.response.GetAllowanceTypeUpdateLogResponseDto;
import org.springframework.data.domain.Page;

public interface AllowanceTypeLogService {
    Page<GetAllowanceTypeUpdateLogResponseDto> getAllowanceTypeUpdateLogs(int page, int size, String sort);
}

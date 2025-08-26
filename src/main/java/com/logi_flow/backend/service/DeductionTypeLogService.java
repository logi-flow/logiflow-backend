package com.logi_flow.backend.service;

import com.logi_flow.backend.dto.deductionTypeLog.response.GetDeductionTypeUpdateLogResponseDto;
import org.springframework.data.domain.Page;

public interface DeductionTypeLogService {
    Page<GetDeductionTypeUpdateLogResponseDto> getDeductionTypeUpdateLogs(int page, int size, String sort);
}

package com.logi_flow.backend.service;

import com.logi_flow.backend.dto.allocationLog.response.GetAllocationStatusLogResponseDto;
import com.logi_flow.backend.dto.allocationLog.response.GetAllocationUpdateLogResponseDto;
import org.springframework.data.domain.Page;

public interface AllocationLogService {
    Page<GetAllocationUpdateLogResponseDto> getAllocationUpdateLog(int page, int size, String sort);

    Page<GetAllocationStatusLogResponseDto> getAllocationStatusLog(int page, int size, String sort);
}

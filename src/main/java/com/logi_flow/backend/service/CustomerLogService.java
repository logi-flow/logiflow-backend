package com.logi_flow.backend.service;

import com.logi_flow.backend.dto.customerLog.response.GetCustomerStatusLogResponseDto;
import com.logi_flow.backend.dto.customerLog.response.GetCustomerUpdateLogResponseDto;
import org.springframework.data.domain.Page;

public interface CustomerLogService {
    Page<GetCustomerUpdateLogResponseDto> getCustomerUpdateLogs(int page, int size, String sort);

    Page<GetCustomerStatusLogResponseDto> getCustomerStatusLogs(int page, int size, String sort);
}

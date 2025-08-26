package com.logi_flow.backend.service;

import com.logi_flow.backend.dto.deliveryLog.response.GetAllDeliveryStatusLogResponseDto;
import com.logi_flow.backend.dto.deliveryLog.response.GetAllDeliveryUpdateLogResponseDto;
import org.springframework.data.domain.Page;

public interface DeliveryLogService {
    Page<GetAllDeliveryUpdateLogResponseDto> getAllDeliveryUpdateLogs(int page, int size, String sort);

    Page<GetAllDeliveryStatusLogResponseDto> getAllDeliveryStatusLogs(int page, int size, String sort);
}

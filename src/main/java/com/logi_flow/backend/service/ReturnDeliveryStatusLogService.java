package com.logi_flow.backend.service;

import com.logi_flow.backend.dto.returnDelivery.response.GetAllReturnDeliveryStatusLogResponseDto;
import com.logi_flow.backend.dto.returnDelivery.response.GetAllReturnDeliveryUpdateLogResponseDto;
import org.springframework.data.domain.Page;

public interface ReturnDeliveryStatusLogService {
    Page<GetAllReturnDeliveryStatusLogResponseDto> getAllReturnDeliveryStatusLog(int page, int size, String sort);
}

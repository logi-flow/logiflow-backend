package com.logi_flow.backend.service;

import com.logi_flow.backend.dto.returnDelivery.response.GetAllReturnDeliveryUpdateLogResponseDto;
import org.springframework.data.domain.Page;

public interface ReturnDeliveryUpdateLogService {
    Page<GetAllReturnDeliveryUpdateLogResponseDto> getAllReturnDeliveryUpdateLog(int page, int size, String sort);
}

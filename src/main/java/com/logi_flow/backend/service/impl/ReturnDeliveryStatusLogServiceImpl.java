package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.dto.returnDelivery.response.GetAllReturnDeliveryStatusLogResponseDto;
import com.logi_flow.backend.service.ReturnDeliveryStatusLogService;
import org.springframework.data.domain.Page;

public class ReturnDeliveryStatusLogServiceImpl implements ReturnDeliveryStatusLogService {
    @Override
    public Page<GetAllReturnDeliveryStatusLogResponseDto> getAllReturnDeliveryStatusLog(int page, int size, String sort) {
        return null;
    }
}

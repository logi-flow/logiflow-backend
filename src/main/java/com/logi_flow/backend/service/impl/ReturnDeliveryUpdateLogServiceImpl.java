package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.dto.returnDelivery.response.GetAllReturnDeliveryUpdateLogResponseDto;
import com.logi_flow.backend.service.ReturnDeliveryUpdateLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReturnDeliveryUpdateLogServiceImpl implements ReturnDeliveryUpdateLogService {
    @Override
    public Page<GetAllReturnDeliveryUpdateLogResponseDto> getAllReturnDeliveryUpdateLog(int page, int size, String sort) {
        return null;
    }
}

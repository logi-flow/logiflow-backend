package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.returnDelivery.request.CreateReturnDeliveryRequestDto;
import com.logi_flow.backend.dto.returnDelivery.request.UpdateReturnDeliveryRequestDto;
import com.logi_flow.backend.dto.returnDelivery.request.UpdateReturnDeliveryStatusRequestDto;
import com.logi_flow.backend.dto.returnDelivery.response.CreateReturnDeliveryResponseDto;
import com.logi_flow.backend.dto.returnDelivery.response.GetAllReturnDeliveryResponseDto;
import com.logi_flow.backend.dto.returnDelivery.response.GetReturnDeliveryDetailResponseDto;
import com.logi_flow.backend.dto.returnDelivery.response.UpdateReturnDeliveryResponseDto;
import com.logi_flow.backend.service.ReturnDeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReturnDeliveryServiceImpl implements ReturnDeliveryService {

    @Override
    public ResponseDto<CreateReturnDeliveryResponseDto> createReturnDelivery(Long deliveryId, CreateReturnDeliveryRequestDto dto, UserPrincipal userPrincipal) {
        return null;
    }

    @Override
    public Page<GetAllReturnDeliveryResponseDto> getAllReturnDelivery(int page, int size, String sort) {
        return null;
    }

    @Override
    public ResponseDto<GetReturnDeliveryDetailResponseDto> getReturnDelivery(Long returnDeliveryId) {
        return null;
    }

    @Override
    public Page<GetAllReturnDeliveryResponseDto> getMyReturnDeliveries(UserPrincipal userPrincipal, int page, int size, String sort) {
        return null;
    }

    @Override
    public ResponseDto<UpdateReturnDeliveryResponseDto> updateReturnDelivery(Long returnDeliveryId, UpdateReturnDeliveryRequestDto dto, UserPrincipal userPrincipal) {
        return null;
    }

    @Override
    public ResponseDto<UpdateReturnDeliveryResponseDto> updateReturnDeliveryStatus(Long returnDeliveryId, UpdateReturnDeliveryStatusRequestDto dto, UserPrincipal userPrincipal) {
        return null;
    }

    @Override
    public ResponseDto<Void> deleteReturnDelivery(UserPrincipal userPrincipal, Long returnDeliveryId) {
        return null;
    }
}

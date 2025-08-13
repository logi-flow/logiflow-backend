package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.delivery.request.CreateDeliveryRequestDto;
import com.logi_flow.backend.dto.delivery.request.UpdateDeliveryRequestDto;
import com.logi_flow.backend.dto.delivery.request.UpdateDeliveryStatusRequestDto;
import com.logi_flow.backend.dto.delivery.request.UpdateIsHiddenRequestDto;
import com.logi_flow.backend.dto.delivery.response.*;
import com.logi_flow.backend.repository.DeliveryRepository;
import com.logi_flow.backend.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;

    @Override
    public ResponseDto<CreateDeliveryResponseDto> createDelivery(CreateDeliveryRequestDto dto, UserPrincipal userPrincipal) {


        return null;
    }

    @Override
    public ResponseDto<List<GetAllDeliveryResponseDto>> getAllDelivery() {
        return null;
    }

    @Override
    public ResponseDto<GetDeliveryDetailResponseDto> getDelivery(Long deliveryId) {
        return null;
    }

    @Override
    public ResponseDto<UpdateDeliveryResponseDto> updateDeliveryIsHidden(Long deliveryId, UpdateIsHiddenRequestDto dto, UserPrincipal userPrincipal) {
        return null;
    }

    @Override
    public ResponseDto<UpdateDeliveryResponseDto> updateDelivery(Long deliveryId, UpdateDeliveryRequestDto dto, UserPrincipal userPrincipal) {
        return null;
    }

    @Override
    public ResponseDto<UpdateDeliveryResponseDto> updateDeliveryStatus(Long deliveryId, UpdateDeliveryStatusRequestDto dto, UserPrincipal userPrincipal) {
        return null;
    }

    @Override
    public ResponseDto<Void> deleteDelivery(Long deliveryId) {
        return null;
    }

    @Override
    public ResponseDto<List<GetAllWaitingDeliveryResponseDto>> getAllWaitingDelivery() {
        return null;
    }

}

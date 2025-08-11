package com.logi_flow.backend.service;

import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.delivery.request.CreateDeliveryRequestDto;
import com.logi_flow.backend.dto.delivery.request.UpdateDeliveryRequestDto;
import com.logi_flow.backend.dto.delivery.response.*;
import jakarta.validation.Valid;

import java.util.List;

public interface DeliveryService {
    ResponseDto<CreateDeliveryResponseDto> createDelivery(CreateDeliveryRequestDto dto);

    ResponseDto<List<GetAllDeliveryResponseDto>> getAllDelivery();

    ResponseDto<GetDeliveryDetailResponseDto> getDelivery(Long deliveryId);

    ResponseDto<UpdateDeliveryResponseDto> updateDelivery(Long deliveryId, UpdateDeliveryRequestDto dto);

    ResponseDto<List<GetAllWaitingDeliveryResponseDto>> getAllWaitingDelivery();

    ResponseDto<?> deleteDelivery(Long deliveryId);
}

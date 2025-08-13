package com.logi_flow.backend.service;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.delivery.request.CreateDeliveryRequestDto;
import com.logi_flow.backend.dto.delivery.request.UpdateDeliveryRequestDto;
import com.logi_flow.backend.dto.delivery.request.UpdateDeliveryStatusRequestDto;
import com.logi_flow.backend.dto.delivery.request.UpdateIsHiddenRequestDto;
import com.logi_flow.backend.dto.delivery.response.*;
import jakarta.validation.Valid;

import java.util.List;

public interface DeliveryService {
    ResponseDto<CreateDeliveryResponseDto> createDelivery(CreateDeliveryRequestDto dto, UserPrincipal userPrincipal);

    ResponseDto<List<GetAllDeliveryResponseDto>> getAllDelivery();

    ResponseDto<GetDeliveryDetailResponseDto> getDelivery(Long deliveryId);

    ResponseDto<UpdateDeliveryResponseDto> updateDeliveryIsHidden(Long deliveryId, UpdateIsHiddenRequestDto dto, UserPrincipal userPrincipal);

    ResponseDto<UpdateDeliveryResponseDto> updateDelivery(Long deliveryId, UpdateDeliveryRequestDto dto, UserPrincipal userPrincipal);

    ResponseDto<UpdateDeliveryResponseDto> updateDeliveryStatus(Long deliveryId, @Valid UpdateDeliveryStatusRequestDto dto, UserPrincipal userPrincipal);

    ResponseDto<List<GetAllWaitingDeliveryResponseDto>> getAllWaitingDelivery();

    ResponseDto<Void> deleteDelivery(Long deliveryId);
}

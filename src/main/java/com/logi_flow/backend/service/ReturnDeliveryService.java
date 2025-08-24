package com.logi_flow.backend.service;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.delivery.response.GetAllWaitingReturnDeliveryResponseDto;
import com.logi_flow.backend.dto.returnDelivery.request.CreateReturnDeliveryRequestDto;
import com.logi_flow.backend.dto.returnDelivery.request.UpdateReturnDeliveryRequestDto;
import com.logi_flow.backend.dto.returnDelivery.request.UpdateReturnDeliveryStatusRequestDto;
import com.logi_flow.backend.dto.returnDelivery.response.CreateReturnDeliveryResponseDto;
import com.logi_flow.backend.dto.returnDelivery.response.GetAllReturnDeliveryResponseDto;
import com.logi_flow.backend.dto.returnDelivery.response.GetReturnDeliveryDetailResponseDto;
import com.logi_flow.backend.dto.returnDelivery.response.UpdateReturnDeliveryResponseDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

public interface ReturnDeliveryService {
    ResponseDto<CreateReturnDeliveryResponseDto> createReturnDelivery(Long deliveryId, @Valid CreateReturnDeliveryRequestDto dto, UserPrincipal userPrincipal);

    Page<GetAllReturnDeliveryResponseDto> getAllReturnDelivery(int page, int size, String sort);

    ResponseDto<GetReturnDeliveryDetailResponseDto> getReturnDelivery(Long returnDeliveryId);

    Page<GetAllReturnDeliveryResponseDto> getMyReturnDeliveries(UserPrincipal userPrincipal, int page, int size, String sort);

    ResponseDto<UpdateReturnDeliveryResponseDto> updateReturnDelivery(Long returnDeliveryId, @Valid UpdateReturnDeliveryRequestDto dto, UserPrincipal userPrincipal);

    ResponseDto<UpdateReturnDeliveryResponseDto> updateReturnDeliveryStatus(Long returnDeliveryId, @Valid UpdateReturnDeliveryStatusRequestDto dto, UserPrincipal userPrincipal);

    ResponseDto<Void> deleteReturnDelivery(UserPrincipal userPrincipal, Long returnDeliveryId);

    Page<GetAllWaitingReturnDeliveryResponseDto> getAllWaitingReturnDelivery(int page, int size, String sort);
}

package com.logi_flow.backend.service;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.delivery.request.CreateDeliveryRequestDto;
import com.logi_flow.backend.dto.delivery.request.UpdateDeliveryRequestDto;
import com.logi_flow.backend.dto.delivery.request.UpdateDeliveryStatusRequestDto;
import com.logi_flow.backend.dto.delivery.request.UpdateIsHiddenRequestDto;
import com.logi_flow.backend.dto.delivery.response.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DeliveryService {
    ResponseDto<CreateDeliveryResponseDto> createDelivery(CreateDeliveryRequestDto dto, UserPrincipal userPrincipal);

    Page<GetAllDeliveryResponseDto> getAllDelivery(int page, int size, String sort);

    ResponseDto<GetDeliveryDetailResponseDto> getDelivery(Long deliveryId);

    Page<GetAllDeliveryResponseDto> getMyDeliveries(UserPrincipal userPrincipal, int page, int size, String sort);

    ResponseDto<UpdateDeliveryResponseDto> updateDeliveryIsHidden(Long deliveryId, UpdateIsHiddenRequestDto dto, UserPrincipal userPrincipal);

    ResponseDto<UpdateDeliveryResponseDto> updateDelivery(Long deliveryId, UpdateDeliveryRequestDto dto, UserPrincipal userPrincipal);

    ResponseDto<UpdateDeliveryResponseDto> updateDeliveryStatus(Long deliveryId, @Valid UpdateDeliveryStatusRequestDto dto, UserPrincipal userPrincipal);

    Page<GetAllWaitingDeliveryResponseDto> getAllWaitingDelivery(int page, int size, String sort);

    ResponseDto<Void> deleteDelivery(Long deliveryId);

    ResponseDto<List<CreateDeliveryResponseDto>> uploadDelivery(MultipartFile file);
}

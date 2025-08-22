package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.common.mapper.PageMapper;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.PageDto;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.delivery.response.GetAllWaitingDeliveryResponseDto;
import com.logi_flow.backend.dto.delivery.response.GetAllWaitingReturnDeliveryResponseDto;
import com.logi_flow.backend.dto.returnDelivery.request.CreateReturnDeliveryRequestDto;
import com.logi_flow.backend.dto.returnDelivery.request.UpdateReturnDeliveryRequestDto;
import com.logi_flow.backend.dto.returnDelivery.request.UpdateReturnDeliveryStatusRequestDto;
import com.logi_flow.backend.dto.returnDelivery.response.CreateReturnDeliveryResponseDto;
import com.logi_flow.backend.dto.returnDelivery.response.GetAllReturnDeliveryResponseDto;
import com.logi_flow.backend.dto.returnDelivery.response.GetReturnDeliveryDetailResponseDto;
import com.logi_flow.backend.dto.returnDelivery.response.UpdateReturnDeliveryResponseDto;
import com.logi_flow.backend.service.ReturnDeliveryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.RETURN_DELIVERY_API)
public class ReturnDeliveryController {
    private final ReturnDeliveryService returnDeliveryService;

    @PostMapping("/{deliveryId}")
    public ResponseEntity<ResponseDto<CreateReturnDeliveryResponseDto>> createReturnDelivery(@PathVariable Long deliveryId, @Valid @RequestBody CreateReturnDeliveryRequestDto dto, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ResponseDto<CreateReturnDeliveryResponseDto> response = returnDeliveryService.createReturnDelivery(deliveryId, dto, userPrincipal);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<PageDto<GetAllReturnDeliveryResponseDto>>> getAllReturnDelivery(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetAllReturnDeliveryResponseDto> result = returnDeliveryService.getAllReturnDelivery(page, size, sort);
        PageDto<GetAllReturnDeliveryResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping("/{returnDeliveryId}")
    public ResponseEntity<ResponseDto<GetReturnDeliveryDetailResponseDto>> getReturnDelivery(@PathVariable Long returnDeliveryId) {
        ResponseDto<GetReturnDeliveryDetailResponseDto> response = returnDeliveryService.getReturnDelivery(returnDeliveryId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping("/me")
    public ResponseEntity<ResponseDto<PageDto<GetAllReturnDeliveryResponseDto>>> getMyReturnDeliveries(
        @AuthenticationPrincipal UserPrincipal userPrincipal,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetAllReturnDeliveryResponseDto> result = returnDeliveryService.getMyReturnDeliveries(userPrincipal, page, size, sort);
        PageDto<GetAllReturnDeliveryResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @PutMapping("/{returnDeliveryId}")
    public ResponseEntity<ResponseDto<UpdateReturnDeliveryResponseDto>> updateReturnDelivery(@PathVariable Long returnDeliveryId, @Valid @RequestBody UpdateReturnDeliveryRequestDto dto, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ResponseDto<UpdateReturnDeliveryResponseDto> response = returnDeliveryService.updateReturnDelivery(returnDeliveryId, dto, userPrincipal);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @PutMapping("/{returnDeliveryId}/status")
    public ResponseEntity<ResponseDto<UpdateReturnDeliveryResponseDto>> updateReturnDeliveryStatus(@PathVariable Long returnDeliveryId, @Valid @RequestBody UpdateReturnDeliveryStatusRequestDto dto, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ResponseDto<UpdateReturnDeliveryResponseDto> response = returnDeliveryService.updateReturnDeliveryStatus(returnDeliveryId, dto, userPrincipal);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @DeleteMapping("/{returnDeliveryId}")
    public ResponseEntity<ResponseDto<Void>> deleteReturnDelivery(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long returnDeliveryId) {
        ResponseDto<Void> response = returnDeliveryService.deleteReturnDelivery(userPrincipal, returnDeliveryId);
        return ResponseDto.toResponseEntity(HttpStatus.NO_CONTENT, response);
    }

    @GetMapping("/waiting")
    public ResponseEntity<ResponseDto<PageDto<GetAllWaitingReturnDeliveryResponseDto>>> getAllWaitingDelivery(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetAllWaitingReturnDeliveryResponseDto> result = returnDeliveryService.getAllWaitingReturnDelivery(page, size, sort);
        PageDto<GetAllWaitingReturnDeliveryResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }
}

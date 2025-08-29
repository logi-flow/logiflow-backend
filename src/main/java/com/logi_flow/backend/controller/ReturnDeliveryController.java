package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.common.mapper.PageMapper;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.PageDto;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.delivery.request.UpdateIsHiddenRequestDto;
import com.logi_flow.backend.dto.delivery.response.GetAllWaitingReturnDeliveryResponseDto;
import com.logi_flow.backend.dto.returnDelivery.request.CreateReturnDeliveryRequestDto;
import com.logi_flow.backend.dto.returnDelivery.request.UpdateReturnDeliveryRequestDto;
import com.logi_flow.backend.dto.returnDelivery.request.UpdateReturnDeliveryStatusRequestDto;
import com.logi_flow.backend.dto.returnDelivery.response.CreateReturnDeliveryResponseDto;
import com.logi_flow.backend.dto.returnDelivery.response.GetAllReturnDeliveryResponseDto;
import com.logi_flow.backend.dto.returnDelivery.response.GetReturnDeliveryDetailResponseDto;
import com.logi_flow.backend.dto.returnDelivery.response.UpdateReturnDeliveryResponseDto;
import com.logi_flow.backend.service.ReturnDeliveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "반품 배송 관리", description = "반품 배송(Return-Delivery) 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.RETURN_DELIVERY_API)
public class ReturnDeliveryController {
    private final ReturnDeliveryService returnDeliveryService;

    @Operation(summary = "신규 반품 배송 생성", description = "새로운 반품 배송 정보를 입력하면 배송 생성")
    @PostMapping("/{deliveryId}")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<ResponseDto<CreateReturnDeliveryResponseDto>> createReturnDelivery(@PathVariable Long deliveryId, @Valid @RequestBody CreateReturnDeliveryRequestDto dto, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ResponseDto<CreateReturnDeliveryResponseDto> response = returnDeliveryService.createReturnDelivery(deliveryId, dto, userPrincipal);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    @Operation(summary = "반품 배송 전체 조회", description = "반품 배송 정보를 전부 조회")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ALLOCATIONS_MANAGER', 'CUSTOMER')")
    public ResponseEntity<ResponseDto<PageDto<GetAllReturnDeliveryResponseDto>>> getAllReturnDelivery(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetAllReturnDeliveryResponseDto> result = returnDeliveryService.getAllReturnDelivery(page, size, sort);
        PageDto<GetAllReturnDeliveryResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "반품 배송 단건 조회", description = "반품 배송 단건에 대한 상세 조회")
    @GetMapping("/{returnDeliveryId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ALLOCATIONS_MANAGER', 'CUSTOMER')")
    public ResponseEntity<ResponseDto<GetReturnDeliveryDetailResponseDto>> getReturnDelivery(@PathVariable Long returnDeliveryId) {
        ResponseDto<GetReturnDeliveryDetailResponseDto> response = returnDeliveryService.getReturnDelivery(returnDeliveryId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "고객사 반품 배송 조회", description = "고객사의 반품 배송 정보를 전부 조회")
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
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

    @Operation(summary = "반품 배송 정보 수정", description = "반품 배송 정보를 수정")
    @PutMapping("/{returnDeliveryId}")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<ResponseDto<UpdateReturnDeliveryResponseDto>> updateReturnDelivery(@PathVariable Long returnDeliveryId, @Valid @RequestBody UpdateReturnDeliveryRequestDto dto, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ResponseDto<UpdateReturnDeliveryResponseDto> response = returnDeliveryService.updateReturnDelivery(returnDeliveryId, dto, userPrincipal);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "반품 배송 상태 수정", description = "반품 배송 상태를 수정")
    @PutMapping("/{returnDeliveryId}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'ALLOCATIONS_MANAGER')")
    public ResponseEntity<ResponseDto<UpdateReturnDeliveryResponseDto>> updateReturnDeliveryStatus(@PathVariable Long returnDeliveryId, @Valid @RequestBody UpdateReturnDeliveryStatusRequestDto dto, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ResponseDto<UpdateReturnDeliveryResponseDto> response = returnDeliveryService.updateReturnDeliveryStatus(returnDeliveryId, dto, userPrincipal);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "반품 배송 삭제", description = "반품 배송 정보를 삭제")
    @DeleteMapping("/{returnDeliveryId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseDto<Void>> deleteReturnDelivery(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long returnDeliveryId) {
        ResponseDto<Void> response = returnDeliveryService.deleteReturnDelivery(userPrincipal, returnDeliveryId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "배차 가능 반품 배송 조회", description = "배차 가능한 반품 배송 정보를 조회")
    @GetMapping("/waiting")
    @PreAuthorize("hasAnyRole('ADMIN', 'ALLOCATIONS_MANAGER')")
    public ResponseEntity<ResponseDto<PageDto<GetAllWaitingReturnDeliveryResponseDto>>> getAllWaitingDelivery(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetAllWaitingReturnDeliveryResponseDto> result = returnDeliveryService.getAllWaitingReturnDelivery(page, size, sort);
        PageDto<GetAllWaitingReturnDeliveryResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "반품 배송 취소", description = "반품 배송 상태를 취소로 수정")
    @PutMapping("/{returnDeliveryId}/cancel")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<ResponseDto<UpdateReturnDeliveryResponseDto>> updateReturnDeliveryStatusCancel(@PathVariable Long returnDeliveryId, @Valid @RequestBody UpdateReturnDeliveryStatusRequestDto dto, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ResponseDto<UpdateReturnDeliveryResponseDto> response = returnDeliveryService.updateReturnDeliveryStatusCancel(returnDeliveryId, dto, userPrincipal);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "반품 배송 정보 숨김", description = "반품 배송 정보를 숨김 처리")
    @PutMapping("/{returnDeliveryId}/is-hidden")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<ResponseDto<UpdateReturnDeliveryResponseDto>> updateReturnDeliveryIsHidden(@PathVariable Long returnDeliveryId, @Valid @RequestBody UpdateIsHiddenRequestDto dto, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ResponseDto<UpdateReturnDeliveryResponseDto> response = returnDeliveryService.updateReturnDeliveryIsHidden(returnDeliveryId, dto, userPrincipal);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }
}

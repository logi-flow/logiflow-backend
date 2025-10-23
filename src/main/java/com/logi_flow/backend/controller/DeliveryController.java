package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.common.mapper.PageMapper;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.PageDto;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.delivery.request.CreateDeliveryRequestDto;
import com.logi_flow.backend.dto.delivery.request.UpdateDeliveryRequestDto;
import com.logi_flow.backend.dto.delivery.request.UpdateDeliveryStatusRequestDto;
import com.logi_flow.backend.dto.delivery.request.UpdateIsHiddenRequestDto;
import com.logi_flow.backend.dto.delivery.response.*;
import com.logi_flow.backend.service.DeliveryService;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Tag(name = "배송 관리", description = "배송(Delivery) 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.DELIVERY_API)
public class DeliveryController {
    private final DeliveryService deliveryService;

    private static final String DELIVERY_ID_API = "/{deliveryId}";
    private static final String MY_DELIVERY_API = "/me";
    private static final String IS_HIDDEN_API = DELIVERY_ID_API + "/isHidden";
    private static final String DELIVERY_STATUS_API = DELIVERY_ID_API + "/status";
    private static final String DELIVERY_CANCEL_API = DELIVERY_ID_API + "/cancel";
    private static final String WAITING_DELIVERY_API = "/waiting";

    @Operation(summary = "신규 배송 생성", description = "새로운 배송 정보를 입력하면 배송 생성")
    @PostMapping
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<ResponseDto<CreateDeliveryResponseDto>> createDelivery(@Valid @RequestBody CreateDeliveryRequestDto dto, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ResponseDto<CreateDeliveryResponseDto> response = deliveryService.createDelivery(dto, userPrincipal);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    @Operation(summary = "배송 전체 조회", description = "배송 정보를 전부 조회")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ALLOCATIONS_MANAGER')")
    public ResponseEntity<ResponseDto<PageDto<GetAllDeliveryResponseDto>>> getAllDelivery(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetAllDeliveryResponseDto> result = deliveryService.getAllDelivery(page, size, sort);
        PageDto<GetAllDeliveryResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "배송 단건 조회", description = "배송 단건에 대한 상세 조회")
    @GetMapping(DELIVERY_ID_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'ALLOCATIONS_MANAGER', 'CUSTOMER')")
    public ResponseEntity<ResponseDto<GetDeliveryDetailResponseDto>> getDelivery(@PathVariable Long deliveryId) {
        ResponseDto<GetDeliveryDetailResponseDto> response = deliveryService.getDelivery(deliveryId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "고객사 배송 조회", description = "고객사의 배송 정보를 전부 조회")
    @GetMapping(MY_DELIVERY_API)
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<ResponseDto<PageDto<GetAllDeliveryResponseDto>>> getMyDeliveries(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetAllDeliveryResponseDto> result = deliveryService.getMyDeliveries(userPrincipal, page, size, sort);
        PageDto<GetAllDeliveryResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "배송 정보 숨김", description = "배송 정보를 숨김 처리")
    @PutMapping(IS_HIDDEN_API)
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<ResponseDto<UpdateDeliveryResponseDto>> updateDeliveryIsHidden(@PathVariable Long deliveryId, @Valid @RequestBody UpdateIsHiddenRequestDto dto, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ResponseDto<UpdateDeliveryResponseDto> response = deliveryService.updateDeliveryIsHidden(deliveryId, dto, userPrincipal);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "배송 정보 수정", description = "배송 정보를 수정")
    @PutMapping(DELIVERY_ID_API)
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<ResponseDto<UpdateDeliveryResponseDto>> updateDelivery(@PathVariable Long deliveryId, @Valid @RequestBody UpdateDeliveryRequestDto dto, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ResponseDto<UpdateDeliveryResponseDto> response = deliveryService.updateDelivery(deliveryId, dto, userPrincipal);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "배송 상태 수정", description = "배송 상태를 수정")
    @PutMapping(DELIVERY_STATUS_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'ALLOCATIONS_MANAGER')")
    public ResponseEntity<ResponseDto<UpdateDeliveryResponseDto>> updateDeliveryStatus(@PathVariable Long deliveryId, @Valid @RequestBody UpdateDeliveryStatusRequestDto dto, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ResponseDto<UpdateDeliveryResponseDto> response = deliveryService.updateDeliveryStatus(deliveryId, dto, userPrincipal);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "배송 취소", description = "배송 상태를 취소로 수정")
    @PutMapping(DELIVERY_CANCEL_API)
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<ResponseDto<UpdateDeliveryResponseDto>> cancelDelivery(@PathVariable Long deliveryId, @Valid @RequestBody UpdateDeliveryStatusRequestDto dto, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ResponseDto<UpdateDeliveryResponseDto> response = deliveryService.cancelDelivery(deliveryId, dto, userPrincipal);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "배송 삭제", description = "배송 정보를 삭제")
    @DeleteMapping(DELIVERY_ID_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'ALLOCATIONS_MANAGER')")
    public ResponseEntity<ResponseDto<Void>> deleteDelivery(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long deliveryId) {
         ResponseDto<Void> response = deliveryService.deleteDelivery(userPrincipal, deliveryId);
        return ResponseDto.toResponseEntity(HttpStatus.NO_CONTENT, response);
    }

    @Operation(summary = "배차 가능 배송 조회", description = "배차 가능한 배송 정보를 조회")
    @GetMapping(WAITING_DELIVERY_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'ALLOCATIONS_MANAGER')")
    public ResponseEntity<ResponseDto<PageDto<GetAllWaitingDeliveryResponseDto>>> getAllWaitingDelivery(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetAllWaitingDeliveryResponseDto> result = deliveryService.getAllWaitingDelivery(page, size, sort);
        PageDto<GetAllWaitingDeliveryResponseDto> response =  PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }


    @Operation(summary = "신규 대용량 배송 생성", description = "엑셀에 새로운 배송 정보들을 입력하고 업로드하면 새로운 배송 등록")
    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<ResponseDto<Map<String, Object>>> uploadDelivery(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ResponseDto<Map<String, Object>>response = deliveryService.uploadDelivery(file, userPrincipal);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

}

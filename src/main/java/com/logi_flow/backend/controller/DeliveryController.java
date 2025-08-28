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

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.DELIVERY_API)
public class DeliveryController {
    private final DeliveryService deliveryService;

    @PostMapping
    public ResponseEntity<ResponseDto<CreateDeliveryResponseDto>> createDelivery(@Valid @RequestBody CreateDeliveryRequestDto dto, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ResponseDto<CreateDeliveryResponseDto> response = deliveryService.createDelivery(dto, userPrincipal);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<PageDto<GetAllDeliveryResponseDto>>> getAllDelivery(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetAllDeliveryResponseDto> result = deliveryService.getAllDelivery(page, size, sort);
        PageDto<GetAllDeliveryResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping("/{deliveryId}")
    public ResponseEntity<ResponseDto<GetDeliveryDetailResponseDto>> getDelivery(@PathVariable Long deliveryId) {
        ResponseDto<GetDeliveryDetailResponseDto> response = deliveryService.getDelivery(deliveryId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping("/me")
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


    @PutMapping("/{deliveryId}/is-hidden")
    public ResponseEntity<ResponseDto<UpdateDeliveryResponseDto>> updateDeliveryIsHidden(@PathVariable Long deliveryId, @Valid @RequestBody UpdateIsHiddenRequestDto dto, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ResponseDto<UpdateDeliveryResponseDto> response = deliveryService.updateDeliveryIsHidden(deliveryId, dto, userPrincipal);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }


    @PutMapping("/{deliveryId}")
    public ResponseEntity<ResponseDto<UpdateDeliveryResponseDto>> updateDelivery(@PathVariable Long deliveryId, @Valid @RequestBody UpdateDeliveryRequestDto dto, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ResponseDto<UpdateDeliveryResponseDto> response = deliveryService.updateDelivery(deliveryId, dto, userPrincipal);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @PutMapping("/{deliveryId}/status")
    public ResponseEntity<ResponseDto<UpdateDeliveryResponseDto>> updateDeliveryStatus(@PathVariable Long deliveryId, @Valid @RequestBody UpdateDeliveryStatusRequestDto dto, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ResponseDto<UpdateDeliveryResponseDto> response = deliveryService.updateDeliveryStatus(deliveryId, dto, userPrincipal);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @PutMapping("/{deliveryId}/cancel")
    public ResponseEntity<ResponseDto<UpdateDeliveryResponseDto>> cancelDelivery(@PathVariable Long deliveryId, @Valid @RequestBody UpdateDeliveryStatusRequestDto dto, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ResponseDto<UpdateDeliveryResponseDto> response = deliveryService.cancelDelivery(deliveryId, dto, userPrincipal);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @DeleteMapping("/{deliveryId}")
    public ResponseEntity<ResponseDto<Void>> deleteDelivery(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long deliveryId) {
         ResponseDto<Void> response = deliveryService.deleteDelivery(userPrincipal, deliveryId);
        return ResponseDto.toResponseEntity(HttpStatus.NO_CONTENT, response);
    }

    @GetMapping("/waiting")
    public ResponseEntity<ResponseDto<PageDto<GetAllWaitingDeliveryResponseDto>>> getAllWaitingDelivery(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetAllWaitingDeliveryResponseDto> result = deliveryService.getAllWaitingDelivery(page, size, sort);
        PageDto<GetAllWaitingDeliveryResponseDto> response =  PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }


    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<ResponseDto<List<CreateDeliveryResponseDto>>> uploadDelivery(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ResponseDto<List<CreateDeliveryResponseDto>> response = deliveryService.uploadDelivery(file, userPrincipal);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

}

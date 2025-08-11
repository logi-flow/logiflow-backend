package com.logi_flow.backend.controller;

import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.delivery.request.CreateDeliveryRequestDto;
import com.logi_flow.backend.dto.delivery.request.UpdateDeliveryRequestDto;
import com.logi_flow.backend.dto.delivery.response.*;
import com.logi_flow.backend.service.DeliveryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/deliveries")
public class DeliveryController {
    private final DeliveryService deliveryService;

    @PostMapping
    public ResponseEntity<ResponseDto<CreateDeliveryResponseDto>> createDelivery(@Valid @RequestBody CreateDeliveryRequestDto dto) {
        ResponseDto<CreateDeliveryResponseDto> response = deliveryService.createDelivery(dto);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<GetAllDeliveryResponseDto>>> getAllDelivery() {
        ResponseDto<List<GetAllDeliveryResponseDto>> response = deliveryService.getAllDelivery();
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping("/{deliveryId}")
    public ResponseEntity<ResponseDto<GetDeliveryDetailResponseDto>> getDelivery(@PathVariable Long deliveryId) {
        ResponseDto<GetDeliveryDetailResponseDto> response = deliveryService.getDelivery(deliveryId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @PutMapping("/{deliveryId}")
    public ResponseEntity<ResponseDto<UpdateDeliveryResponseDto>> updateDelivery(@PathVariable Long deliveryId, @Valid @RequestBody UpdateDeliveryRequestDto dto) {
        ResponseDto<UpdateDeliveryResponseDto> response = deliveryService.updateDelivery(deliveryId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @DeleteMapping("/{deliveryId}")
    public ResponseEntity<ResponseDto<?>> deleteDelivery(@PathVariable Long deliveryId) {
         ResponseDto<?> response = deliveryService.deleteDelivery(deliveryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    // 배차 대기 목록 조회
    @GetMapping("/waiting")
    public ResponseEntity<ResponseDto<List<GetAllWaitingDeliveryResponseDto>>> getAllWaitingDelivery() {
        ResponseDto<List<GetAllWaitingDeliveryResponseDto>> response = deliveryService.getAllWaitingDelivery();
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }


}

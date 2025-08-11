package com.logi_flow.backend.controller;

import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.delivery.request.CreateDeliveryRequestDto;
import com.logi_flow.backend.dto.delivery.request.UpdateDeliveryRequestDto;
import com.logi_flow.backend.dto.delivery.response.*;
import com.logi_flow.backend.service.DeliveryService;
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
    public ResponseEntity<ResponseDto<CreateDeliveryResponseDto>> createDelivery(@RequestBody CreateDeliveryRequestDto dto) {
        ResponseDto<CreateDeliveryResponseDto> response = deliveryService.createDelivery(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<GetAllDeliveryResponseDto>>> getAllDelivery() {
        ResponseDto<List<GetAllDeliveryResponseDto>> response = deliveryService.getAllDelivery();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{deliveryId}")
    public ResponseEntity<ResponseDto<GetDeliveryDetailResponseDto>> getDelivery(@PathVariable Long deliveryId) {
        ResponseDto<GetDeliveryDetailResponseDto> response = deliveryService.getDelivery(deliveryId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{deliveryId}")
    public ResponseEntity<ResponseDto<UpdateDeliveryResponseDto>> updateDelivery(@PathVariable Long deliveryId, @RequestBody UpdateDeliveryRequestDto dto) {
        ResponseDto<UpdateDeliveryResponseDto> response = deliveryService.updateDelivery(deliveryId, dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{deliveryId}")
    public ResponseEntity<ResponseDto<?>> deleteDelivery(@PathVariable Long deliveryId) {
        deliveryService.deleteDelivery(deliveryId);
        return ResponseEntity.noContent().build();
    }

    // 배차 대기 목록 조회
    @GetMapping("/waiting")
    public ResponseEntity<ResponseDto<List<GetAllWaitingDeliveryResponseDto>>> getAllWaitingDelivery() {
        ResponseDto<List<GetAllWaitingDeliveryResponseDto>> response = deliveryService.getAllWaitingDelivery();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}

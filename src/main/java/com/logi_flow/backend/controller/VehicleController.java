package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.vehicle.request.CreateVehicleRequestDto;
import com.logi_flow.backend.dto.vehicle.request.UpdateVehicleRequestDto;
import com.logi_flow.backend.dto.vehicle.response.CreateVehicleResponseDto;
import com.logi_flow.backend.dto.vehicle.response.GetAllVehicleResponseDto;
import com.logi_flow.backend.dto.vehicle.response.GetVehicleDetailResponseDto;
import com.logi_flow.backend.dto.vehicle.response.UpdateVehicleResponseDto;
import com.logi_flow.backend.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.VEHICLE_API)
public class VehicleController {

    private final VehicleService vehicleService;

    private static final String VEHICLE_ID_API = "/{vehicleId}";

    @PostMapping
    public ResponseEntity<ResponseDto<CreateVehicleResponseDto>> createVehicle(
            @RequestBody CreateVehicleRequestDto dto
    ) {
        ResponseDto<CreateVehicleResponseDto> response = vehicleService.createVehicle(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(VEHICLE_ID_API)
    public ResponseEntity<ResponseDto<UpdateVehicleResponseDto>> updateVehicle(
            @PathVariable Long vehicleId,
            @RequestBody UpdateVehicleRequestDto dto
    ) {
        ResponseDto<UpdateVehicleResponseDto> response = vehicleService.updateVehicle(vehicleId, dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<GetAllVehicleResponseDto>> getAllVehicle(){
        ResponseDto<GetAllVehicleResponseDto> response = vehicleService.getAllVehicle();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(VEHICLE_ID_API)
    public ResponseEntity<ResponseDto<GetVehicleDetailResponseDto>> getVehicleDetail(
            @PathVariable Long vehicleId
    ) {
        ResponseDto<GetVehicleDetailResponseDto> response = vehicleService.getVehicleDetail(vehicleId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(VEHICLE_ID_API)
    public ResponseEntity<ResponseDto<?>> deleteVehicle(
            @PathVariable Long vehicleId
    ) {
        ResponseDto<?> response = vehicleService.delete(vehicleId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

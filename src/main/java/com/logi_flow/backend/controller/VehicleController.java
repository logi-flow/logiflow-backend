package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.common.mapper.PageMapper;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.PageDto;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.vehicle.request.CreateVehicleRequestDto;
import com.logi_flow.backend.dto.vehicle.request.UpdateVehicleRequestDto;
import com.logi_flow.backend.dto.vehicle.request.UpdateVehicleStatusRequestDto;
import com.logi_flow.backend.dto.vehicle.response.CreateVehicleResponseDto;
import com.logi_flow.backend.dto.vehicle.response.GetAllVehicleResponseDto;
import com.logi_flow.backend.dto.vehicle.response.GetVehicleDetailResponseDto;
import com.logi_flow.backend.dto.vehicle.response.UpdateVehicleResponseDto;
import com.logi_flow.backend.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.VEHICLE_API)
public class VehicleController {

    private final VehicleService vehicleService;

    private static final String VEHICLE_ID_API = "/{vehicleId}";
    private static final String UPDATE_STATUS_API = VEHICLE_ID_API + "/status";

    @PostMapping
    public ResponseEntity<ResponseDto<CreateVehicleResponseDto>> createVehicle(
            @Valid @RequestBody CreateVehicleRequestDto dto
    ) {
        ResponseDto<CreateVehicleResponseDto> response = vehicleService.createVehicle(dto);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    @PutMapping(VEHICLE_ID_API)
    public ResponseEntity<ResponseDto<UpdateVehicleResponseDto>> updateVehicle(
            @PathVariable Long vehicleId,
            @Valid @RequestBody UpdateVehicleRequestDto dto
    ) {
        ResponseDto<UpdateVehicleResponseDto> response = vehicleService.updateVehicle(vehicleId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @PutMapping(UPDATE_STATUS_API)
    public ResponseEntity<ResponseDto<UpdateVehicleResponseDto>> updateVehicleStatus(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long vehicleId,
            @Valid @RequestBody UpdateVehicleStatusRequestDto dto
    ) {
        ResponseDto<UpdateVehicleResponseDto> response = vehicleService.updateVehicleStatus(userPrincipal, vehicleId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<PageDto<GetAllVehicleResponseDto>>> getAllVehicle(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ){
        Page<GetAllVehicleResponseDto> result = vehicleService.getAllVehicle(page, size, sort);
        PageDto<GetAllVehicleResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping(VEHICLE_ID_API)
    public ResponseEntity<ResponseDto<GetVehicleDetailResponseDto>> getVehicleDetail(
            @PathVariable Long vehicleId
    ) {
        ResponseDto<GetVehicleDetailResponseDto> response = vehicleService.getVehicleDetail(vehicleId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @DeleteMapping(VEHICLE_ID_API)
    public ResponseEntity<ResponseDto<?>> deleteVehicle(
            @PathVariable Long vehicleId
    ) {
        ResponseDto<?> response = vehicleService.delete(vehicleId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}

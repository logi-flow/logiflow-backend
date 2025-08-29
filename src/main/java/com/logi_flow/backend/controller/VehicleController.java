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

@Tag(name = "차량 관리", description = "차량(Vehicle) 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.VEHICLE_API)
public class VehicleController {

    private final VehicleService vehicleService;

    private static final String VEHICLE_ID_API = "/{vehicleId}";
    private static final String UPDATE_STATUS_API = VEHICLE_ID_API + "/status";

    @Operation(summary = "차량 생성", description = "신규 차량을 생성")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ALLOCATIONS_MANAGER')")
    public ResponseEntity<ResponseDto<CreateVehicleResponseDto>> createVehicle(
            @Valid @RequestBody CreateVehicleRequestDto dto
    ) {
        ResponseDto<CreateVehicleResponseDto> response = vehicleService.createVehicle(dto);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    @Operation(summary = "차량 수정", description = "차량 정보를 수정")
    @PutMapping(VEHICLE_ID_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'ALLOCATIONS_MANAGER')")
    public ResponseEntity<ResponseDto<UpdateVehicleResponseDto>> updateVehicle(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long vehicleId,
            @Valid @RequestBody UpdateVehicleRequestDto dto
    ) {
        ResponseDto<UpdateVehicleResponseDto> response = vehicleService.updateVehicle(userPrincipal, vehicleId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "차량 상태 수정", description = "차량의 상태를 수정, 임시차량 배정 및 배정에 관련된 자동화 로직 포함")
    @PutMapping(UPDATE_STATUS_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'ALLOCATIONS_MANAGER')")
    public ResponseEntity<ResponseDto<UpdateVehicleResponseDto>> updateVehicleStatus(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long vehicleId,
            @Valid @RequestBody UpdateVehicleStatusRequestDto dto
    ) {
        ResponseDto<UpdateVehicleResponseDto> response = vehicleService.updateVehicleStatus(userPrincipal, vehicleId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "모든 차량 조회", description = "모든 차량 정보 조회")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ALLOCATIONS_MANAGER')")
    public ResponseEntity<ResponseDto<PageDto<GetAllVehicleResponseDto>>> getAllVehicle(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ){
        Page<GetAllVehicleResponseDto> result = vehicleService.getAllVehicle(page, size, sort);
        PageDto<GetAllVehicleResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "차량 세부정보 조회", description = "차량 세부 사항을 조회")
    @GetMapping(VEHICLE_ID_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'ALLOCATIONS_MANAGER', 'DRIVER')")
    public ResponseEntity<ResponseDto<GetVehicleDetailResponseDto>> getVehicleDetail(
            @PathVariable Long vehicleId
    ) {
        ResponseDto<GetVehicleDetailResponseDto> response = vehicleService.getVehicleDetail(vehicleId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "차량 삭제", description = "차량 삭제, 자동 배정 비활성화 로직 포함")
    @DeleteMapping(VEHICLE_ID_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'ALLOCATIONS_MANAGER')")
    public ResponseEntity<ResponseDto<Void>> deleteVehicle(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long vehicleId
    ) {
        ResponseDto<Void> response = vehicleService.deleteVehicle(userPrincipal, vehicleId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}

package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.common.mapper.PageMapper;
import com.logi_flow.backend.dto.PageDto;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.vehicle.response.VehicleStatusLogResponseDto;
import com.logi_flow.backend.dto.vehicle.response.VehicleUpdateLogResponseDto;
import com.logi_flow.backend.service.VehicleLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.VEHICLE_API + "/logs")
public class VehicleLogController {

    private final VehicleLogService vehicleLogService;

    private static final String STATUS_API = "/status";
    private static final String UPDATE_API = "/update";

    @GetMapping(STATUS_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'ALLOCATIONS_MANAGER')")
    public ResponseEntity<ResponseDto<PageDto<VehicleStatusLogResponseDto>>> getStatusLog(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<VehicleStatusLogResponseDto> result = vehicleLogService.getStatusLog(page, size, sort);
        PageDto<VehicleStatusLogResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping(UPDATE_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'ALLOCATIONS_MANAGER')")
    public ResponseEntity<ResponseDto<PageDto<VehicleUpdateLogResponseDto>>> getUpdateLog(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<VehicleUpdateLogResponseDto> result = vehicleLogService.getUpdateLog(page, size, sort);
        PageDto<VehicleUpdateLogResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }
}

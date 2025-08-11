package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.driver.request.CreateDriverRequestDto;
import com.logi_flow.backend.dto.driver.request.UpdateDriverRequestDto;
import com.logi_flow.backend.dto.driver.response.CreateDriverResponseDto;
import com.logi_flow.backend.dto.driver.response.GetAllDriverResponseDto;
import com.logi_flow.backend.dto.driver.response.GetDriverDetailResponseDto;
import com.logi_flow.backend.dto.driver.response.UpdateDriverResponseDto;
import com.logi_flow.backend.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.DRIVER_API)
public class DriverController {

    private final DriverService driverService;

    @PostMapping
    public ResponseEntity<ResponseDto<CreateDriverResponseDto>> createDriver(
            @RequestBody CreateDriverRequestDto dto
    ) {
        ResponseDto<CreateDriverResponseDto> response = driverService.createDriver(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{driverId}")
    public ResponseEntity<ResponseDto<UpdateDriverResponseDto>> updateDriver(
            @PathVariable Long driverId,
            @RequestBody UpdateDriverRequestDto dto
    ) {
        ResponseDto<UpdateDriverResponseDto> response = driverService.updateDriver(driverId, dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<GetAllDriverResponseDto>> getAllDriver() {
        ResponseDto<GetAllDriverResponseDto> response = driverService.getAllDriver();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{driverId}")
    public ResponseEntity<ResponseDto<GetDriverDetailResponseDto>> getDriverDetail(
            @PathVariable Long driverId
    ) {
        ResponseDto<GetDriverDetailResponseDto> response = driverService.getDriverDetail(driverId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{driverId}")
    public ResponseEntity<ResponseDto<?>> deleteDriver(
            @PathVariable Long driverId
    ) {
        ResponseDto<?> response = driverService.deleteDriver(driverId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
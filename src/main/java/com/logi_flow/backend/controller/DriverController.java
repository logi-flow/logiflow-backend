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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.DRIVER_API)
public class DriverController {

    private final DriverService driverService;

    private static final String DRIVER_ID_API = "/{driverId}";

    @PostMapping
    public ResponseEntity<ResponseDto<CreateDriverResponseDto>> createDriver(
            @Valid @RequestBody CreateDriverRequestDto dto
    ) {
        ResponseDto<CreateDriverResponseDto> response = driverService.createDriver(dto);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    @PutMapping(DRIVER_ID_API)
    public ResponseEntity<ResponseDto<UpdateDriverResponseDto>> updateDriver(
            @PathVariable Long driverId,
            @Valid @RequestBody UpdateDriverRequestDto dto
    ) {
        ResponseDto<UpdateDriverResponseDto> response = driverService.updateDriver(driverId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<GetAllDriverResponseDto>> getAllDriver() {
        ResponseDto<GetAllDriverResponseDto> response = driverService.getAllDriver();
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping(DRIVER_ID_API)
    public ResponseEntity<ResponseDto<GetDriverDetailResponseDto>> getDriverDetail(
            @PathVariable Long driverId
    ) {
        ResponseDto<GetDriverDetailResponseDto> response = driverService.getDriverDetail(driverId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @DeleteMapping(DRIVER_ID_API)
    public ResponseEntity<ResponseDto<?>> deleteDriver(
            @PathVariable Long driverId
    ) {
        ResponseDto<?> response = driverService.deleteDriver(driverId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}
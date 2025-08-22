package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.driver.request.*;
import com.logi_flow.backend.dto.driver.response.*;
import com.logi_flow.backend.service.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.DRIVER_API)
public class DriverController {

    private final DriverService driverService;

    private static final String MY_INFO_API = "/me";
    private static final String DRIVER_ID_API = "/{driverId}";
    private static final String UPDATE_STATUS_API = DRIVER_ID_API + "/status";
    private static final String UPDATE_PAY_API = DRIVER_ID_API + "/pay";

    @PostMapping
    public ResponseEntity<ResponseDto<CreateDriverResponseDto>> createDriver(
            @Valid @RequestBody CreateDriverRequestDto dto
    ) {
        ResponseDto<CreateDriverResponseDto> response = driverService.createDriver(dto);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    @PutMapping(MY_INFO_API)
    public ResponseEntity<ResponseDto<UpdateDriverResponseDto>> updateDriver(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody UpdateDriverRequestDto dto
    ) {
        ResponseDto<UpdateDriverResponseDto> response = driverService.updateDriver(userPrincipal, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @PutMapping(DRIVER_ID_API)
    public ResponseEntity<ResponseDto<UpdateDriverResponseDto>> updateDriverByAdmin(
            @PathVariable Long driverId,
            @Valid @RequestBody UpdateDriverByAdminRequestDto dto
    ) {
        ResponseDto<UpdateDriverResponseDto> response = driverService.updateDriverByAdmin(driverId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @PutMapping(UPDATE_PAY_API)
    public ResponseEntity<ResponseDto<UpdateDriverPayResponseDto>> updateDriverPay(
            @PathVariable Long driverId,
            @Valid @RequestBody UpdateDriverPayRequestDto dto
    ) {
        ResponseDto<UpdateDriverPayResponseDto> response = driverService.updateDriverPay(driverId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @PutMapping(UPDATE_STATUS_API)
    public ResponseEntity<ResponseDto<UpdateDriverResponseDto>> updateDriverStatus(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long driverId,
            @Valid @RequestBody UpdateDriverStatusRequestDto dto
    ) {
        ResponseDto<UpdateDriverResponseDto> response = driverService.updateDriverStatus(userPrincipal, driverId, dto);
        return  ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<GetAllDriverResponseDto>>> getAllDriver() {
        ResponseDto<List<GetAllDriverResponseDto>> response = driverService.getAllDriver();
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
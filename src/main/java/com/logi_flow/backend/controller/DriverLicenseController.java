package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.driver.request.CreateDriverLicenseRequestDto;
import com.logi_flow.backend.dto.driver.request.UpdateDriverLicenseRequestDto;
import com.logi_flow.backend.dto.driver.response.CreateDriverLicenseResponseDto;
import com.logi_flow.backend.dto.driver.response.UpdateDriverLicenseResponseDto;
import com.logi_flow.backend.service.DriverLicenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.DRIVER_API)
public class DriverLicenseController {

    private final DriverLicenseService driverLicenseService;

    private static final String CREATE_LICENSE_API = "/{driverId}/licenses";
    private static final String UPDATE_LICENSE_API = CREATE_LICENSE_API + "/{licenseId}";


    @PostMapping(CREATE_LICENSE_API)
    public ResponseEntity<ResponseDto<CreateDriverLicenseResponseDto>> createDriverLicense(
            @PathVariable Long driverId,
            @RequestBody CreateDriverLicenseRequestDto dto
    ) {
        ResponseDto<CreateDriverLicenseResponseDto> response = driverLicenseService.createDriverLicense(driverId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(UPDATE_LICENSE_API)
    public ResponseEntity<ResponseDto<UpdateDriverLicenseResponseDto>> updateDriverLicense(
            @PathVariable Long driverId,
            @PathVariable Long licenseId,
            @RequestBody UpdateDriverLicenseRequestDto dto
    ) {
        ResponseDto<UpdateDriverLicenseResponseDto> response = driverLicenseService.updateDriverLicense(driverId, licenseId, dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

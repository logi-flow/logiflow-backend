package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.driver.request.CreateDriverLicenseRequestDto;
import com.logi_flow.backend.dto.driver.request.UpdateDriverLicenseRequestDto;
import com.logi_flow.backend.dto.driver.response.CreateDriverLicenseResponseDto;
import com.logi_flow.backend.dto.driver.response.UpdateDriverLicenseResponseDto;
import com.logi_flow.backend.service.DriverLicenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.DRIVER_API)
public class DriverLicenseController {

    private final DriverLicenseService driverLicenseService;

    private static final String CREATE_LICENSE_API = "/{driverId}/licenses";
    private static final String UPDATE_LICENSE_API = CREATE_LICENSE_API + "/{licenseId}";


    @PostMapping(CREATE_LICENSE_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<ResponseDto<CreateDriverLicenseResponseDto>> createDriverLicense(
            @PathVariable Long driverId,
            @Valid @RequestBody CreateDriverLicenseRequestDto dto
    ) {
        ResponseDto<CreateDriverLicenseResponseDto> response = driverLicenseService.createDriverLicense(driverId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    @PutMapping(UPDATE_LICENSE_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
    public ResponseEntity<ResponseDto<UpdateDriverLicenseResponseDto>> updateDriverLicense(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long driverId,
            @PathVariable Long licenseId,
            @Valid @RequestBody UpdateDriverLicenseRequestDto dto
    ) {
        ResponseDto<UpdateDriverLicenseResponseDto> response = driverLicenseService.updateDriverLicense(userPrincipal, driverId, licenseId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }
}

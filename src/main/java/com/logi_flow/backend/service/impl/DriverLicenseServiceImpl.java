package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.driver.request.CreateDriverLicenseRequestDto;
import com.logi_flow.backend.dto.driver.request.UpdateDriverLicenseRequestDto;
import com.logi_flow.backend.dto.driver.response.CreateDriverLicenseResponseDto;
import com.logi_flow.backend.dto.driver.response.UpdateDriverLicenseResponseDto;
import com.logi_flow.backend.service.DriverLicenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DriverLicenseServiceImpl implements DriverLicenseService {
    @Override
    public ResponseDto<CreateDriverLicenseResponseDto> createDriverLicense(Long driverId, CreateDriverLicenseRequestDto dto) {
        return null;
    }

    @Override
    public ResponseDto<UpdateDriverLicenseResponseDto> updateDriverLicense(Long driverId, Long licenseId, UpdateDriverLicenseRequestDto dto) {
        return null;
    }
}

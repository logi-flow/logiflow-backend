package com.logi_flow.backend.service;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.driver.request.CreateDriverLicenseRequestDto;
import com.logi_flow.backend.dto.driver.request.UpdateDriverLicenseRequestDto;
import com.logi_flow.backend.dto.driver.response.CreateDriverLicenseResponseDto;
import com.logi_flow.backend.dto.driver.response.UpdateDriverLicenseResponseDto;

public interface DriverLicenseService {
    ResponseDto<CreateDriverLicenseResponseDto> createDriverLicense(Long driverId, CreateDriverLicenseRequestDto dto);

    ResponseDto<UpdateDriverLicenseResponseDto> updateDriverLicense(UserPrincipal userPrincipal, Long driverId, Long licenseId, UpdateDriverLicenseRequestDto dto);

    void noticeExpiredDate();
}

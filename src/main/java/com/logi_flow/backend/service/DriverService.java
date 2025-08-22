package com.logi_flow.backend.service;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.driver.request.*;
import com.logi_flow.backend.dto.driver.response.*;
import jakarta.validation.Valid;

import java.util.List;

public interface DriverService {
    ResponseDto<CreateDriverResponseDto> createDriver(@Valid CreateDriverRequestDto dto);

    ResponseDto<UpdateDriverResponseDto> updateDriver(UserPrincipal userPrincipal, @Valid UpdateDriverRequestDto dto);

    ResponseDto<List<GetAllDriverResponseDto>> getAllDriver();

    ResponseDto<GetDriverDetailResponseDto> getDriverDetail(Long driverId);

    ResponseDto<?> deleteDriver(Long driverId);

    ResponseDto<UpdateDriverResponseDto> updateDriverStatus(UserPrincipal userPrincipal, Long driverId, @Valid UpdateDriverStatusRequestDto dto);

    ResponseDto<UpdateDriverPayResponseDto> updateDriverPay(Long driverId, @Valid UpdateDriverPayRequestDto dto);

    ResponseDto<UpdateDriverResponseDto> updateDriverByAdmin(Long driverId, @Valid UpdateDriverByAdminRequestDto dto);
}

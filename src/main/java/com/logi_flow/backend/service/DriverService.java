package com.logi_flow.backend.service;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.driver.request.*;
import com.logi_flow.backend.dto.driver.response.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

public interface DriverService {
    ResponseDto<CreateDriverResponseDto> createDriver(@Valid CreateDriverRequestDto dto);

    ResponseDto<UpdateDriverResponseDto> updateDriver(UserPrincipal userPrincipal, @Valid UpdateDriverRequestDto dto);

    Page<GetAllDriverResponseDto> getAllDriver(int page, int size, String sort);

    ResponseDto<GetDriverDetailResponseDto> getDriverDetail(Long driverId);

    ResponseDto<GetDriverDetailResponseDto> getMyInfo(UserPrincipal userPrincipal);

    ResponseDto<?> deleteDriver(Long driverId);

    ResponseDto<UpdateDriverResponseDto> updateDriverStatus(UserPrincipal userPrincipal, Long driverId, @Valid UpdateDriverStatusRequestDto dto);

    ResponseDto<UpdateDriverPayResponseDto> updateDriverPay(UserPrincipal userPrincipal, Long driverId, @Valid UpdateDriverPayRequestDto dto);

    ResponseDto<UpdateDriverResponseDto> updateDriverByAdmin(UserPrincipal userPrincipal, Long driverId, @Valid UpdateDriverByAdminRequestDto dto);
}

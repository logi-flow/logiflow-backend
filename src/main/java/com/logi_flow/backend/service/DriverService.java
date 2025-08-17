package com.logi_flow.backend.service;

import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.driver.request.CreateDriverRequestDto;
import com.logi_flow.backend.dto.driver.request.UpdateDriverRequestDto;
import com.logi_flow.backend.dto.driver.response.CreateDriverResponseDto;
import com.logi_flow.backend.dto.driver.response.GetAllDriverResponseDto;
import com.logi_flow.backend.dto.driver.response.GetDriverDetailResponseDto;
import com.logi_flow.backend.dto.driver.response.UpdateDriverResponseDto;
import jakarta.validation.Valid;

import java.util.List;

public interface DriverService {
    ResponseDto<CreateDriverResponseDto> createDriver(@Valid CreateDriverRequestDto dto);

    ResponseDto<UpdateDriverResponseDto> updateDriver(Long driverId, @Valid UpdateDriverRequestDto dto);

    ResponseDto<List<GetAllDriverResponseDto>> getAllDriver();

    ResponseDto<GetDriverDetailResponseDto> getDriverDetail(Long driverId);

    ResponseDto<?> deleteDriver(Long driverId);
}

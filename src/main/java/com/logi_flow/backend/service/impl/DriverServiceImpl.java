package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.driver.request.CreateDriverRequestDto;
import com.logi_flow.backend.dto.driver.request.UpdateDriverRequestDto;
import com.logi_flow.backend.dto.driver.response.CreateDriverResponseDto;
import com.logi_flow.backend.dto.driver.response.GetAllDriverResponseDto;
import com.logi_flow.backend.dto.driver.response.GetDriverDetailResponseDto;
import com.logi_flow.backend.dto.driver.response.UpdateDriverResponseDto;
import com.logi_flow.backend.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DriverServiceImpl implements DriverService {
    @Override
    public ResponseDto<CreateDriverResponseDto> createDriver(CreateDriverRequestDto dto) {
        return null;
    }

    @Override
    public ResponseDto<UpdateDriverResponseDto> updateDriver(Long driverId, UpdateDriverRequestDto dto) {
        return null;
    }

    @Override
    public ResponseDto<GetAllDriverResponseDto> getAllDriver() {
        return null;
    }

    @Override
    public ResponseDto<GetDriverDetailResponseDto> getDriverDetail(Long driverId) {
        return null;
    }

    @Override
    public ResponseDto<?> deleteDriver(Long driverId) {
        return null;
    }
}

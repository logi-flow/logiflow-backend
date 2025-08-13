package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.driverAllowance.request.CreateDriverAllowanceRequestDto;
import com.logi_flow.backend.dto.driverAllowance.request.UpdateDriverAllowanceRequestDto;
import com.logi_flow.backend.dto.driverAllowance.response.CreateDriverAllowanceResponseDto;
import com.logi_flow.backend.dto.driverAllowance.response.GetDriverAllowanceDetailResponseDto;
import com.logi_flow.backend.dto.driverAllowance.response.UpdateDriverAllowanceResponseDto;
import com.logi_flow.backend.service.DriverAllowanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DriverAllowanceServiceImpl implements DriverAllowanceService {
    @Override
    public ResponseDto<CreateDriverAllowanceResponseDto> createDriverAllowance(Long payrollId, CreateDriverAllowanceRequestDto dto) {
        return null;
    }

    @Override
    public ResponseDto<GetDriverAllowanceDetailResponseDto> getDriverAllowance(Long payrollId) {
        return null;
    }

    @Override
    public ResponseDto<UpdateDriverAllowanceResponseDto> updateDriverAllowance(UserPrincipal userPrincipal, Long payrollId, Long allowanceId, UpdateDriverAllowanceRequestDto dto) {
        return null;
    }

    @Override
    public ResponseDto<Void> deleteDriverAllowance(UserPrincipal userPrincipal, Long payrollId, Long allowanceId) {
        return null;
    }
}

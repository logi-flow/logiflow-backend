package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.driverPayroll.request.CreateDriverPayrollRequestDto;
import com.logi_flow.backend.dto.driverPayroll.request.UpdateDriverPayrollRequestDto;
import com.logi_flow.backend.dto.driverPayroll.request.UpdateDriverPayrollStatusRequestDto;
import com.logi_flow.backend.dto.driverPayroll.response.*;
import com.logi_flow.backend.service.DriverPayrollService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DriverPayrollServiceImpl implements DriverPayrollService {
    @Override
    public ResponseDto<CreateDriverPayrollResponseDto> createDriverPayroll(CreateDriverPayrollRequestDto dto) {
        return null;
    }

    @Override
    public Page<GetAllDriverPayrollResponseDto> getAllDriverPayroll(int page, int size, String sort) {
        return null;
    }

    @Override
    public ResponseDto<GetDriverPayrollDetailResponseDto> getDriverPayrollDetail(Long payrollId) {
        return null;
    }

    @Override
    public Page<GetAllDriverPayrollResponseDto> getMyPayrolls(UserPrincipal userPrincipal, int page, int size, String sort) {
        return null;
    }

    @Override
    public ResponseDto<UpdateDriverPayrollStatusResponseDto> updateDriverPayrollStatus(UserPrincipal userPrincipal, Long payrollId, UpdateDriverPayrollStatusRequestDto dto) {
        return null;
    }

    @Override
    public ResponseDto<UpdateDriverPayrollResponseDto> updateDriverPayroll(UserPrincipal userPrincipal, Long payrollId, UpdateDriverPayrollRequestDto dto) {
        return null;
    }

    @Override
    public ResponseDto<Void> deleteDriverPayroll(UserPrincipal userPrincipal, Long payrollId) {
        return null;
    }
}

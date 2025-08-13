package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.driverDeduction.request.CreateDriverDeductionRequestDto;
import com.logi_flow.backend.dto.driverDeduction.request.UpdateDriverDeductionRequestDto;
import com.logi_flow.backend.dto.driverDeduction.response.CreateDriverDeductionResponseDto;
import com.logi_flow.backend.dto.driverDeduction.response.GetDriverDeductionDetailResponseDto;
import com.logi_flow.backend.dto.driverDeduction.response.UpdateDriverDeductionResponseDto;
import com.logi_flow.backend.service.DriverDeductionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DriverDeductionServiceImpl implements DriverDeductionService {
    @Override
    public ResponseDto<CreateDriverDeductionResponseDto> createDriverDeduction(Long payrollId, CreateDriverDeductionRequestDto dto) {
        return null;
    }

    @Override
    public ResponseDto<GetDriverDeductionDetailResponseDto> getDriverDeduction(Long payrollId) {
        return null;
    }

    @Override
    public ResponseDto<UpdateDriverDeductionResponseDto> updateDriverDeduction(UserPrincipal userPrincipal, Long payrollId, Long deductionId, UpdateDriverDeductionRequestDto dto) {
        return null;
    }

    @Override
    public ResponseDto<Void> deleteDriverDeduction(UserPrincipal userPrincipal, Long payrollId, Long deductionId) {
        return null;
    }
}

package com.logi_flow.backend.service;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.driverAllowance.request.CreateDriverAllowanceRequestDto;
import com.logi_flow.backend.dto.driverAllowance.request.UpdateDriverAllowanceRequestDto;
import com.logi_flow.backend.dto.driverAllowance.response.CreateDriverAllowanceResponseDto;
import com.logi_flow.backend.dto.driverAllowance.response.GetDriverAllowanceDetailResponseDto;
import com.logi_flow.backend.dto.driverAllowance.response.UpdateDriverAllowanceResponseDto;
import jakarta.validation.Valid;

public interface DriverAllowanceService {
    ResponseDto<CreateDriverAllowanceResponseDto> createDriverAllowance(Long payrollId, @Valid CreateDriverAllowanceRequestDto dto);
    ResponseDto<GetDriverAllowanceDetailResponseDto> getDriverAllowance(Long payrollId);
    ResponseDto<UpdateDriverAllowanceResponseDto> updateDriverAllowance(UserPrincipal userPrincipal, Long payrollId, Long allowanceId, @Valid UpdateDriverAllowanceRequestDto dto);
    ResponseDto<Void> deleteDriverAllowance(UserPrincipal userPrincipal, Long payrollId, Long allowanceId);
}

package com.logi_flow.backend.service;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.driverPayroll.request.CreateDriverPayrollRequestDto;
import com.logi_flow.backend.dto.driverPayroll.request.UpdateDriverPayrollRequestDto;
import com.logi_flow.backend.dto.driverPayroll.request.UpdateDriverPayrollStatusRequestDto;
import com.logi_flow.backend.dto.driverPayroll.response.*;
import jakarta.validation.Valid;

public interface DriverPayrollService {
    ResponseDto<CreateDriverPayrollResponseDto> createDriverPayroll(CreateDriverPayrollRequestDto dto);
    ResponseDto<GetAllDriverPayrollResponseDto> getAllDriverPayroll();
    ResponseDto<GetDriverPayrollDetailResponseDto> getDriverPayrollDetail(Long payrollId);
    ResponseDto<GetAllDriverPayrollResponseDto> getMyPayrolls(UserPrincipal userPrincipal);
    ResponseDto<UpdateDriverPayrollStatusResponseDto> updateDriverPayrollStatus(UserPrincipal userPrincipal, Long payrollId, @Valid UpdateDriverPayrollStatusRequestDto dto);
    ResponseDto<UpdateDriverPayrollResponseDto> updateDriverPayroll(UserPrincipal userPrincipal, Long payrollId, @Valid UpdateDriverPayrollRequestDto dto);
    ResponseDto<Void> deleteDriverPayroll(UserPrincipal userPrincipal, Long payrollId);
}

package com.logi_flow.backend.service;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.driverPayroll.request.CreateDriverPayrollRequestDto;
import com.logi_flow.backend.dto.driverPayroll.request.UpdateDriverPayrollRequestDto;
import com.logi_flow.backend.dto.driverPayroll.request.UpdateDriverPayrollStatusRequestDto;
import com.logi_flow.backend.dto.driverPayroll.response.*;
import com.logi_flow.backend.entity.DriverPayroll;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

public interface DriverPayrollService {
    ResponseDto<CreateDriverPayrollResponseDto> createDriverPayroll(CreateDriverPayrollRequestDto dto);
    Page<GetAllDriverPayrollResponseDto> getAllDriverPayroll(int page, int size, String sort);
    ResponseDto<GetDriverPayrollDetailResponseDto> getDriverPayrollDetail(Long payrollId);
    Page<GetAllDriverPayrollResponseDto> getMyPayrolls(UserPrincipal userPrincipal, int page, int size, String sort);
    ResponseDto<UpdateDriverPayrollStatusResponseDto> updateDriverPayrollStatus(UserPrincipal userPrincipal, Long payrollId, @Valid UpdateDriverPayrollStatusRequestDto dto);
    ResponseDto<UpdateDriverPayrollResponseDto> updateDriverPayroll(UserPrincipal userPrincipal, Long payrollId, @Valid UpdateDriverPayrollRequestDto dto);
    ResponseDto<Void> deleteDriverPayroll(UserPrincipal userPrincipal, Long payrollId);
    DriverPayroll getDriverPayroll(Long payrollId);
    DriverPayroll getDriverPayrollForUpdate(Long payrollId);
}

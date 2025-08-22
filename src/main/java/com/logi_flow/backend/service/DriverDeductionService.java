package com.logi_flow.backend.service;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.driverDeduction.request.CreateDriverDeductionRequestDto;
import com.logi_flow.backend.dto.driverDeduction.request.UpdateDriverDeductionRequestDto;
import com.logi_flow.backend.dto.driverDeduction.response.CreateDriverDeductionResponseDto;
import com.logi_flow.backend.dto.driverDeduction.response.GetDriverDeductionDetailResponseDto;
import com.logi_flow.backend.dto.driverDeduction.response.UpdateDriverDeductionResponseDto;
import jakarta.validation.Valid;

import java.util.List;

public interface DriverDeductionService {
    ResponseDto<CreateDriverDeductionResponseDto> createDriverDeduction(Long payrollId, @Valid CreateDriverDeductionRequestDto dto);
    ResponseDto<GetDriverDeductionDetailResponseDto> getDriverDeduction(Long payrollId);
    ResponseDto<List<UpdateDriverDeductionResponseDto>> updateDriverDeduction(UserPrincipal userPrincipal, Long payrollId, List<UpdateDriverDeductionRequestDto.Item> items);
}

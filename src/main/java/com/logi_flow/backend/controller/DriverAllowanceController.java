package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.driverAllowance.request.CreateDriverAllowanceRequestDto;
import com.logi_flow.backend.dto.driverAllowance.request.UpdateDriverAllowanceRequestDto;
import com.logi_flow.backend.dto.driverAllowance.response.CreateDriverAllowanceResponseDto;
import com.logi_flow.backend.dto.driverAllowance.response.GetDriverAllowanceDetailResponseDto;
import com.logi_flow.backend.dto.driverAllowance.response.UpdateDriverAllowanceResponseDto;
import com.logi_flow.backend.service.DriverAllowanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.PAYROLL_API)
public class DriverAllowanceController {
    private final DriverAllowanceService driverAllowanceService;

    private final static String ALLOWANCE_API = "/{payrollId}/allowance";

    @PostMapping(ALLOWANCE_API)
    public ResponseEntity<ResponseDto<CreateDriverAllowanceResponseDto>> createDriverAllowance(
            @PathVariable Long payrollId,
            @Valid @RequestBody CreateDriverAllowanceRequestDto dto
    ) {
        ResponseDto<CreateDriverAllowanceResponseDto> response = driverAllowanceService.createDriverAllowance(payrollId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    @GetMapping(ALLOWANCE_API)
    public ResponseEntity<ResponseDto<GetDriverAllowanceDetailResponseDto>> getDriverAllowance(
            @PathVariable Long payrollId
    ) {
        ResponseDto<GetDriverAllowanceDetailResponseDto> response = driverAllowanceService.getDriverAllowance(payrollId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @PutMapping(ALLOWANCE_API)
    public ResponseEntity<ResponseDto<List<UpdateDriverAllowanceResponseDto>>> updateDriverAllowance(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long payrollId,
            @Valid @RequestBody UpdateDriverAllowanceRequestDto dto
    ) {
        ResponseDto<List<UpdateDriverAllowanceResponseDto>> response = driverAllowanceService.updateDriverAllowance(userPrincipal, payrollId, dto.getItems());
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }
}

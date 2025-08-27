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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.PAYROLL_API)
public class DriverAllowanceController {
    private final DriverAllowanceService driverAllowanceService;

    private final static String ALLOWANCE_API = "/{payrollId}/allowances";
    private final static String ALLOWANCE_ID_API = "/{payrollId}/allowances/{allowanceId}";

    @PostMapping(ALLOWANCE_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'HUMAN_RESOURCES_MANAGER')")
    public ResponseEntity<ResponseDto<CreateDriverAllowanceResponseDto>> createDriverAllowance(
            @PathVariable Long payrollId,
            @Valid @RequestBody CreateDriverAllowanceRequestDto dto
    ) {
        ResponseDto<CreateDriverAllowanceResponseDto> response = driverAllowanceService.createDriverAllowance(payrollId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    @GetMapping(ALLOWANCE_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'HUMAN_RESOURCES_MANAGER')")
    public ResponseEntity<ResponseDto<List<GetDriverAllowanceDetailResponseDto>>> getDriverAllowance(
            @PathVariable Long payrollId
    ) {
        ResponseDto<List<GetDriverAllowanceDetailResponseDto>> response = driverAllowanceService.getDriverAllowance(payrollId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @PutMapping(ALLOWANCE_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'HUMAN_RESOURCES_MANAGER')")
    public ResponseEntity<ResponseDto<List<UpdateDriverAllowanceResponseDto>>> updateDriverAllowance(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long payrollId,
            @Valid @RequestBody UpdateDriverAllowanceRequestDto dto
    ) {
        ResponseDto<List<UpdateDriverAllowanceResponseDto>> response = driverAllowanceService.updateDriverAllowance(userPrincipal, payrollId, dto.getItems());
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @DeleteMapping(ALLOWANCE_ID_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'HUMAN_RESOURCES_MANAGER')")
    public ResponseEntity<ResponseDto<Void>> deleteDriverAllowance(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long payrollId,
            @PathVariable Long allowanceId
    ) {
        ResponseDto<Void> response = driverAllowanceService.deleteDriverAllowance(userPrincipal, payrollId, allowanceId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }
}

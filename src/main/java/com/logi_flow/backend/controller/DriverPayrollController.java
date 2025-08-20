package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.common.mapper.PageMapper;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.PageDto;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.driverPayroll.request.CreateDriverPayrollRequestDto;
import com.logi_flow.backend.dto.driverPayroll.request.UpdateDriverPayrollRequestDto;
import com.logi_flow.backend.dto.driverPayroll.request.UpdateDriverPayrollStatusRequestDto;
import com.logi_flow.backend.dto.driverPayroll.response.*;
import com.logi_flow.backend.service.DriverPayrollService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.PAYROLL_API)
public class DriverPayrollController {
    private final DriverPayrollService driverPayrollService;

    private final static String PAYROLL_ID_API = "/{payrollId}";
    private final static String UPDATE_STATUS_API = PAYROLL_ID_API + "/status";
    private final static String MY_PAYROLL_API = "/me";

    @PostMapping
    public ResponseEntity<ResponseDto<CreateDriverPayrollResponseDto>> createDriverPayroll(
            @RequestBody CreateDriverPayrollRequestDto dto
    ) {
        ResponseDto<CreateDriverPayrollResponseDto> response = driverPayrollService.createDriverPayroll(dto);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<PageDto<GetAllDriverPayrollResponseDto>>> getAllDriverPayroll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetAllDriverPayrollResponseDto> result = driverPayrollService.getAllDriverPayroll(page, size, sort);
        PageDto<GetAllDriverPayrollResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping(PAYROLL_ID_API)
    public ResponseEntity<ResponseDto<GetDriverPayrollDetailResponseDto>> getDriverPayrollDetail(
            @PathVariable Long payrollId
    ) {
        ResponseDto<GetDriverPayrollDetailResponseDto> response = driverPayrollService.getDriverPayrollDetail(payrollId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping(MY_PAYROLL_API)
    public ResponseEntity<ResponseDto<PageDto<GetAllDriverPayrollResponseDto>>> getMyPayrolls(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetAllDriverPayrollResponseDto> result = driverPayrollService.getMyPayrolls(userPrincipal, page, size, sort);
        PageDto<GetAllDriverPayrollResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @PutMapping(UPDATE_STATUS_API)
    public ResponseEntity<ResponseDto<UpdateDriverPayrollStatusResponseDto>> updateDriverPayrollStatus(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long payrollId,
            @Valid @RequestBody UpdateDriverPayrollStatusRequestDto dto
    ) {
        ResponseDto<UpdateDriverPayrollStatusResponseDto> response = driverPayrollService.updateDriverPayrollStatus(userPrincipal, payrollId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @PutMapping(PAYROLL_ID_API)
    public ResponseEntity<ResponseDto<UpdateDriverPayrollResponseDto>> updateDriverPayroll(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long payrollId,
            @Valid @RequestBody UpdateDriverPayrollRequestDto dto
    ) {
        ResponseDto<UpdateDriverPayrollResponseDto> response = driverPayrollService.updateDriverPayroll(userPrincipal, payrollId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @DeleteMapping(PAYROLL_ID_API)
    public ResponseEntity<ResponseDto<Void>> deleteDriverPayroll(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long payrollId
    ) {
        ResponseDto<Void> response = driverPayrollService.deleteDriverPayroll(userPrincipal, payrollId);
        return ResponseDto.toResponseEntity(HttpStatus.NO_CONTENT, response);
    }
}

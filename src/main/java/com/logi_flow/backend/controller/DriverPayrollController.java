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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@Tag(name = "DriverPayroll", description = "기사 급여대장 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.PAYROLL_API)
public class DriverPayrollController {
    private final DriverPayrollService driverPayrollService;

    private final static String PAYROLL_ID_API = "/{payrollId}";
    private final static String UPDATE_STATUS_API = PAYROLL_ID_API + "/status";
    private final static String MY_PAYROLL_API = "/me";
    private final static String MY_PAYROLL_PAYROLL_ID_API = MY_PAYROLL_API + PAYROLL_ID_API;

    @Operation(summary = "급여대장 생성", description = "급여대장 생성")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HUMAN_RESOURCES_MANAGER')")
    public ResponseEntity<ResponseDto<CreateDriverPayrollResponseDto>> createDriverPayroll(
            @RequestBody CreateDriverPayrollRequestDto dto
    ) {
        ResponseDto<CreateDriverPayrollResponseDto> response = driverPayrollService.createDriverPayroll(dto);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    @Operation(summary = "급여대장 전체 조회", description = "전체 급여대장 목록 조회")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HUMAN_RESOURCES_MANAGER')")
    public ResponseEntity<ResponseDto<PageDto<GetAllDriverPayrollResponseDto>>> getAllDriverPayroll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetAllDriverPayrollResponseDto> result = driverPayrollService.getAllDriverPayroll(page, size, sort);
        PageDto<GetAllDriverPayrollResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "급여대장 상세 조회", description = "특정 급여대장의 상세 정보 조회")
    @GetMapping(PAYROLL_ID_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'HUMAN_RESOURCES_MANAGER')")
    public ResponseEntity<ResponseDto<GetDriverPayrollDetailResponseDto>> getDriverPayrollDetail(
            @PathVariable Long payrollId
    ) {
        ResponseDto<GetDriverPayrollDetailResponseDto> response = driverPayrollService.getDriverPayrollDetail(payrollId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "본인 급여대장 전체 조회", description = "기사 본인의 전체 급여대장 목록 조회")
    @GetMapping(MY_PAYROLL_API)
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<ResponseDto<PageDto<GetAllDriverPayrollResponseDto>>> getAllMyPayroll(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetAllDriverPayrollResponseDto> result = driverPayrollService.getMyPayrolls(userPrincipal, page, size, sort);
        PageDto<GetAllDriverPayrollResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "본인 급여대장 상세 조회", description = "기사 본인의 특정 급여대장의 상세 정보 조회")
    @GetMapping(MY_PAYROLL_PAYROLL_ID_API)
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<ResponseDto<GetDriverPayrollDetailResponseDto>> getMyPayrollDetail(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long payrollId
    ) throws AccessDeniedException {
        ResponseDto<GetDriverPayrollDetailResponseDto> response = driverPayrollService.getMyPayrollDetail(userPrincipal, payrollId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "급여대장 수정", description = "특정 급여대장의 정보 수정")
    @PutMapping(PAYROLL_ID_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'HUMAN_RESOURCES_MANAGER')")
    public ResponseEntity<ResponseDto<UpdateDriverPayrollResponseDto>> updateDriverPayroll(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long payrollId,
            @Valid @RequestBody UpdateDriverPayrollRequestDto dto
    ) {
        ResponseDto<UpdateDriverPayrollResponseDto> response = driverPayrollService.updateDriverPayroll(userPrincipal, payrollId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "급여대장 상태 수정", description = "특정 급여대장의 상태 수정")
    @PutMapping(UPDATE_STATUS_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'HUMAN_RESOURCES_MANAGER')")
    public ResponseEntity<ResponseDto<UpdateDriverPayrollStatusResponseDto>> updateDriverPayrollStatus(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long payrollId,
            @Valid @RequestBody UpdateDriverPayrollStatusRequestDto dto
    ) {
        ResponseDto<UpdateDriverPayrollStatusResponseDto> response = driverPayrollService.updateDriverPayrollStatus(userPrincipal, payrollId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "급여대장 삭제", description = "특정 급여대장의 상태를 삭제로 변경")
    @DeleteMapping(PAYROLL_ID_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'HUMAN_RESOURCES_MANAGER')")
    public ResponseEntity<ResponseDto<Void>> deleteDriverPayroll(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long payrollId
    ) {
        ResponseDto<Void> response = driverPayrollService.deleteDriverPayroll(userPrincipal, payrollId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }
}

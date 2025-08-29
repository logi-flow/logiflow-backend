package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.common.mapper.PageMapper;
import com.logi_flow.backend.dto.PageDto;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.driverPayrollLog.response.GetDriverPayrollStatusLogResponseDto;
import com.logi_flow.backend.dto.driverPayrollLog.response.GetDriverPayrollUpdateLogResponseDto;
import com.logi_flow.backend.service.DriverPayrollLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "급여대장 관리", description = "급여대장 내역 변경 이력 조회 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.PAYROLL_API)
public class DriverPayrollLogController {
    private final DriverPayrollLogService driverPayrollLogService;

    private static final String UPDATE_API = "/logs/update";
    private static final String STATUS_API = "/logs/status";

    @Operation(summary = "급여대장 변경 이력 조회", description = "급여대장의 변경 이력 조회")
    @GetMapping(UPDATE_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'HUMAN_RESOURCES_MANAGER')")
    public ResponseEntity<ResponseDto<PageDto<GetDriverPayrollUpdateLogResponseDto>>> getDriverPayrollUpdateLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetDriverPayrollUpdateLogResponseDto> result = driverPayrollLogService.getDriverPayrollUpdateLogs(page, size, sort);
        PageDto<GetDriverPayrollUpdateLogResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "급여대장 상태 변경 이력 조회", description = "급여대장의 상태 변경 이력 조회")
    @GetMapping(STATUS_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'HUMAN_RESOURCES_MANAGER')")
    public ResponseEntity<ResponseDto<PageDto<GetDriverPayrollStatusLogResponseDto>>> getDriverPayrollStatusLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetDriverPayrollStatusLogResponseDto> result = driverPayrollLogService.getDriverPayrollStatusLogs(page, size, sort);
        PageDto<GetDriverPayrollStatusLogResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }
}

package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.common.mapper.PageMapper;
import com.logi_flow.backend.dto.PageDto;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.driverAllowanceLog.response.GetDriverAllowanceUpdateLogResponseDto;
import com.logi_flow.backend.service.DriverAllowanceLogService;
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

@Tag(name = "DriverAllowanceUpdateLog", description = "수당 내역 변경 이력 조회 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.PAYROLL_API)
public class DriverAllowanceLogController {
    private final DriverAllowanceLogService driverAllowanceLogService;

    private static final String UPDATE_API = "/allowances/logs/update";

    @Operation(summary = "수당 내역 변경 이력 조회", description = "수당 내역의 변경 이력 조회")
    @GetMapping(UPDATE_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'HUMAN_RESOURCES_MANAGER')")
    public ResponseEntity<ResponseDto<PageDto<GetDriverAllowanceUpdateLogResponseDto>>> getDriverAllowanceUpdateLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetDriverAllowanceUpdateLogResponseDto> result = driverAllowanceLogService.getDriverAllowanceUpdateLogs(page, size, sort);
        PageDto<GetDriverAllowanceUpdateLogResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }
}

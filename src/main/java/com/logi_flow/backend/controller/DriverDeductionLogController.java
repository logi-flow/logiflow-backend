package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.common.mapper.PageMapper;
import com.logi_flow.backend.dto.PageDto;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.driverDeductionLog.response.GetDriverDeductionUpdateLogResponseDto;
import com.logi_flow.backend.service.DriverDeductionLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.PAYROLL_API)
public class DriverDeductionLogController {
    private final DriverDeductionLogService driverDeductionLogService;

    private static final String UPDATE_API = "/deductions/logs/update";

    @GetMapping(UPDATE_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'HUMAN_RESOURCES_MANAGER')")
    public ResponseEntity<ResponseDto<PageDto<GetDriverDeductionUpdateLogResponseDto>>> getDriverDeductionUpdateLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetDriverDeductionUpdateLogResponseDto> result = driverDeductionLogService.getDriverDeductionUpdateLogs(page, size, sort);
        PageDto<GetDriverDeductionUpdateLogResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }
}

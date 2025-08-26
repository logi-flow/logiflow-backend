package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.common.mapper.PageMapper;
import com.logi_flow.backend.dto.PageDto;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.employeeLog.response.GetEmployeeStatusLogResponseDto;
import com.logi_flow.backend.dto.employeeLog.response.GetEmployeeUpdateLogResponseDto;
import com.logi_flow.backend.service.EmployeeLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.EMPLOYEE_API)
public class EmployeeLogController {
    private final EmployeeLogService employeeLogService;

    private static final String UPDATE_LOG = "/logs/update";
    private static final String STATUS_LOG = "/logs/status";

    @GetMapping(UPDATE_LOG)
    public ResponseEntity<ResponseDto<PageDto<GetEmployeeUpdateLogResponseDto>>> getEmployeeUpdateLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetEmployeeUpdateLogResponseDto> result = employeeLogService.getEmployeeUpdateLogs(page, size, sort);
        PageDto<GetEmployeeUpdateLogResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping(STATUS_LOG)
    public ResponseEntity<ResponseDto<PageDto<GetEmployeeStatusLogResponseDto>>> getEmployeeStatusLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetEmployeeStatusLogResponseDto> result = employeeLogService.getEmployeeStatusLogs(page, size, sort);
        PageDto<GetEmployeeStatusLogResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }
}

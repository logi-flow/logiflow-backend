package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.common.mapper.PageMapper;
import com.logi_flow.backend.dto.PageDto;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.driver.response.DriverStatusLogResponseDto;
import com.logi_flow.backend.dto.driver.response.DriverUpdateLogResponseDto;
import com.logi_flow.backend.service.DriverLogService;
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

@Tag(name = "기사 로그 관리", description = "기사(Driver) 관련 로그 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.DRIVER_API + "/logs")
public class DriverLogController {

    private final DriverLogService driverLogService;

    private static final String STATUS_API = "/status";
    private static final String UPDATE_API = "/update";

    @Operation(summary = "기사 상태 변경 조회", description = "로그 조회")
    @GetMapping(STATUS_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'ALLOCATIONS_MANAGER')")
    public ResponseEntity<ResponseDto<PageDto<DriverStatusLogResponseDto>>> getStatusLog(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<DriverStatusLogResponseDto> result = driverLogService.getStatusLog(page, size, sort);
        PageDto<DriverStatusLogResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "기사 정보 변경 조회", description = "로그 조회")
    @GetMapping(UPDATE_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'ALLOCATIONS_MANAGER')")
    public ResponseEntity<ResponseDto<PageDto<DriverUpdateLogResponseDto>>> getUpdateLog(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<DriverUpdateLogResponseDto> result = driverLogService.getUpdateLog(page, size, sort);
        PageDto<DriverUpdateLogResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }
}

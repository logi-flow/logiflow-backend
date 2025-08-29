package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.common.mapper.PageMapper;
import com.logi_flow.backend.dto.PageDto;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.allocationLog.response.GetAllocationStatusLogResponseDto;
import com.logi_flow.backend.dto.allocationLog.response.GetAllocationUpdateLogResponseDto;
import com.logi_flow.backend.service.AllocationLogService;
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

@Tag(name = "배차 로그 관리", description = "배차 로그(AllocationLog) 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.ALLOCATION_LOG_API)
public class AllocationLogController {

    private final AllocationLogService allocationLogService;

    private static final String ALLOCATION_UPDATE_LOG_API = "/update";
    private static final String ALLOCATION_STATUS_LOG_API = "/status";

    @Operation(summary = "배차 수정 로그 조회", description = "배차 수정 로그 전체 목록 조회")
    @GetMapping(ALLOCATION_UPDATE_LOG_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'ALLOCATIONS_MANAGER')")
    public ResponseEntity<ResponseDto<PageDto<GetAllocationUpdateLogResponseDto>>> getAllocationUpdateLog(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetAllocationUpdateLogResponseDto> result = allocationLogService.getAllocationUpdateLog(page, size, sort);
        PageDto<GetAllocationUpdateLogResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "배차 상태 로그 조회", description = "배차 상태 로그 전체 목록 조회")
    @GetMapping(ALLOCATION_STATUS_LOG_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'ALLOCATIONS_MANAGER')")
    public ResponseEntity<ResponseDto<PageDto<GetAllocationStatusLogResponseDto>>> getAllocationStatusLog(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetAllocationStatusLogResponseDto> result = allocationLogService.getAllocationStatusLog(page, size, sort);
        PageDto<GetAllocationStatusLogResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

}

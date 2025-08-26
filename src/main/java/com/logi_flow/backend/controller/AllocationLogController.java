package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.mapper.PageMapper;
import com.logi_flow.backend.dto.PageDto;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.allocationLog.response.GetAllocationStatusLogResponseDto;
import com.logi_flow.backend.dto.allocationLog.response.GetAllocationUpdateLogResponseDto;
import com.logi_flow.backend.service.AllocationLogService;
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
@RequestMapping("/api/v1/allocation/logs")
public class AllocationLogController {

    private final AllocationLogService allocationLogService;

    @GetMapping("/update")
    public ResponseEntity<ResponseDto<PageDto<GetAllocationUpdateLogResponseDto>>> getAllocationUpdateLog(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetAllocationUpdateLogResponseDto> result = allocationLogService.getAllocationUpdateLog(page, size, sort);
        PageDto<GetAllocationUpdateLogResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping("/status")
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

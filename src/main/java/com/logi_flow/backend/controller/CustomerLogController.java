package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.common.mapper.PageMapper;
import com.logi_flow.backend.dto.PageDto;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.customerLog.response.GetCustomerStatusLogResponseDto;
import com.logi_flow.backend.dto.customerLog.response.GetCustomerUpdateLogResponseDto;
import com.logi_flow.backend.service.CustomerLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "고객사 로그 관리", description = "고객사(Customer) 관련 로그 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.CUSTOMER_API)
public class CustomerLogController {
    private final CustomerLogService customerLogService;

    private static final String UPDATE_LOG = "/logs/update";
    private static final String STATUS_LOG = "/logs/status";

    @Operation(summary = "고객사 정보 변경 조회", description = "로그 조회")
    @GetMapping(UPDATE_LOG)
    public ResponseEntity<ResponseDto<PageDto<GetCustomerUpdateLogResponseDto>>> getCustomerUpdateLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetCustomerUpdateLogResponseDto> result = customerLogService.getCustomerUpdateLogs(page, size, sort);
        PageDto<GetCustomerUpdateLogResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "고객사 상태 변경 조회", description = "로그 조회")
    @GetMapping(STATUS_LOG)
    public ResponseEntity<ResponseDto<PageDto<GetCustomerStatusLogResponseDto>>> getCustomerStatusLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetCustomerStatusLogResponseDto> result = customerLogService.getCustomerStatusLogs(page, size, sort);
        PageDto<GetCustomerStatusLogResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }
}

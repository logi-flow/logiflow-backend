package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.common.mapper.PageMapper;
import com.logi_flow.backend.dto.PageDto;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.contractLog.response.GetContractStatusLogResponseDto;
import com.logi_flow.backend.dto.contractLog.response.GetContractUpdateLogResponseDto;
import com.logi_flow.backend.service.ContractLogService;
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

@Tag(name = "계약 로그 관리", description = "계약 로그(ContractLog) 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.CONTRACT_LOG_API)
public class ContractLogController {

    private final ContractLogService contractLogService;

    private static final String CONTRACT_UPDATE_LOG_API = "/update";
    private static final String CONTRACT_STATUS_LOG_API = "/status";

    @Operation(summary = "계약 수정 로그 조회", description = "계약 수정 로그 전체 목록 조회")
    @GetMapping(CONTRACT_UPDATE_LOG_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'CONTRACTS_MANAGER')")
    public ResponseEntity<ResponseDto<PageDto<GetContractUpdateLogResponseDto>>> getContractUpdateLog(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetContractUpdateLogResponseDto> result = contractLogService.getContractUpdateLog(page, size, sort);
        PageDto<GetContractUpdateLogResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "계약 상태 로그 조회", description = "계약 상태 로그 전체 목록 조회")
    @GetMapping(CONTRACT_STATUS_LOG_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'CONTRACTS_MANAGER')")
    public ResponseEntity<ResponseDto<PageDto<GetContractStatusLogResponseDto>>> getContractStatusLog(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetContractStatusLogResponseDto> result = contractLogService.getContractStatusLog(page, size, sort);
        PageDto<GetContractStatusLogResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

}

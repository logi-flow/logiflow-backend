package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.mapper.PageMapper;
import com.logi_flow.backend.dto.PageDto;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.contractLog.response.GetContractStatusLogResponseDto;
import com.logi_flow.backend.dto.contractLog.response.GetContractUpdateLogResponseDto;
import com.logi_flow.backend.service.ContractLogService;
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
@RequestMapping("/api/v1/contract/logs")
public class ContractLogController {

    private final ContractLogService contractLogService;

    @GetMapping("/update")
    public ResponseEntity<ResponseDto<PageDto<GetContractUpdateLogResponseDto>>> getContractUpdateLog(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetContractUpdateLogResponseDto> result = contractLogService.getContractUpdateLog(page, size, sort);
        PageDto<GetContractUpdateLogResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping("/status")
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

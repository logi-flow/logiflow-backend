package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.common.mapper.PageMapper;
import com.logi_flow.backend.dto.PageDto;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.allowanceTypeLog.response.GetAllowanceTypeUpdateLogResponseDto;
import com.logi_flow.backend.service.AllowanceTypeLogService;
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
@RequestMapping(ApiMappingPattern.ALLOWANCE_API)
public class AllowanceTypeLogController {
    private final AllowanceTypeLogService allowanceTypeLogService;

    private static final String UPDATE_LOG = "/logs/update";

    @GetMapping(UPDATE_LOG)
    public ResponseEntity<ResponseDto<PageDto<GetAllowanceTypeUpdateLogResponseDto>>> getAllowanceTypeUpdateLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetAllowanceTypeUpdateLogResponseDto> result = allowanceTypeLogService.getAllowanceTypeUpdateLogs(page, size, sort);
        PageDto<GetAllowanceTypeUpdateLogResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }
}

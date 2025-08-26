package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.common.mapper.PageMapper;
import com.logi_flow.backend.dto.PageDto;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.deductionTypeLog.response.GetDeductionTypeUpdateLogResponseDto;
import com.logi_flow.backend.service.DeductionTypeLogService;
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
@RequestMapping(ApiMappingPattern.DEDUCTION_API)
public class DeductionTypeLogController {
    private final DeductionTypeLogService deductionTypeLogService;

    private static final String UPDATE_LOG = "/logs/update";

    @GetMapping(UPDATE_LOG)
    public ResponseEntity<ResponseDto<PageDto<GetDeductionTypeUpdateLogResponseDto>>> getDeductionTypeUpdateLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetDeductionTypeUpdateLogResponseDto> result = deductionTypeLogService.getDeductionTypeUpdateLogs(page, size, sort);
        PageDto<GetDeductionTypeUpdateLogResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }
}

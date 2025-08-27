package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.common.mapper.PageMapper;
import com.logi_flow.backend.dto.PageDto;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.retunDeliveryLog.response.GetAllReturnDeliveryStatusLogResponseDto;
import com.logi_flow.backend.dto.retunDeliveryLog.response.GetAllReturnDeliveryUpdateLogResponseDto;
import com.logi_flow.backend.service.ReturnDeliveryLogService;
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
@RequestMapping(ApiMappingPattern.RETURN_DELIVERY_LOG_API)
public class ReturnDeliveryLogController {
    private final ReturnDeliveryLogService returnDeliveryLogService;

    @GetMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'HUMAN_RESOURCES_MANAGER')")
    public ResponseEntity<ResponseDto<PageDto<GetAllReturnDeliveryUpdateLogResponseDto>>> getAllReturnDeliveryUpdateLogs(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetAllReturnDeliveryUpdateLogResponseDto> result = returnDeliveryLogService.getAllReturnDeliveryUpdateLogs(page, size, sort);
        PageDto<GetAllReturnDeliveryUpdateLogResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping("/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'HUMAN_RESOURCES_MANAGER')")
    public ResponseEntity<ResponseDto<PageDto<GetAllReturnDeliveryStatusLogResponseDto>>> getAllReturnDeliveryStatusLogs(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetAllReturnDeliveryStatusLogResponseDto> result = returnDeliveryLogService.getAllReturnDeliveryStatusLogs(page, size, sort);
        PageDto<GetAllReturnDeliveryStatusLogResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }
}

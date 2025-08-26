package com.logi_flow.backend.controller;


import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.common.mapper.PageMapper;
import com.logi_flow.backend.dto.PageDto;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.deliveryLog.response.GetAllDeliveryStatusLogResponseDto;
import com.logi_flow.backend.dto.deliveryLog.response.GetAllDeliveryUpdateLogResponseDto;
import com.logi_flow.backend.service.DeliveryLogService;
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
@RequestMapping(ApiMappingPattern.DELIVERY_LOG_API)
public class DeliveryLogController {
    private final DeliveryLogService deliveryLogService;

    @GetMapping("/update")
    public ResponseEntity<ResponseDto<PageDto<GetAllDeliveryUpdateLogResponseDto>>> getAllDeliveryUpdateLogs(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetAllDeliveryUpdateLogResponseDto> result = deliveryLogService.getAllDeliveryUpdateLogs(page, size, sort);
        PageDto<GetAllDeliveryUpdateLogResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping("/status")
    public ResponseEntity<ResponseDto<PageDto<GetAllDeliveryStatusLogResponseDto>>> getAllDeliveryStatusLogs(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetAllDeliveryStatusLogResponseDto> result = deliveryLogService.getAllDeliveryStatusLogs(page, size, sort);
        PageDto<GetAllDeliveryStatusLogResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }
}

package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.common.mapper.PageMapper;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.PageDto;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.alert.response.AlertResponseDto;
import com.logi_flow.backend.service.AlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "알림 관리", description = "알림(Alert) 조회 API")
@RestController
@RequestMapping(ApiMappingPattern.ALERT_API)
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    private static final String MY_ALERT_API = "/me";

    @Operation(summary = "사용자별 알림 조회", description = "사용자별 전체 알림 목록 조회")
    @GetMapping(MY_ALERT_API)
    public ResponseEntity<ResponseDto<PageDto<AlertResponseDto>>> getMyAlerts(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {
        Page<AlertResponseDto> result = alertService.getMyAlerts(userPrincipal, page, size, sort);
        PageDto<AlertResponseDto> response = PageMapper.toPageDto(result, sort);
        return  ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

}

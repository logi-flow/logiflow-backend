package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.stats.driverJoinLeave.response.GetDriverJoinLeaveResponseDto;
import com.logi_flow.backend.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.STATS_API)
public class StatsController {
    private final StatsService statsService;

    private final static String DRIVER_JOIN_LEAVE_API = "/drivers/join-leave";

    @GetMapping(DRIVER_JOIN_LEAVE_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'HUMAN_RESOURCES_MANAGER')")
    public ResponseEntity<ResponseDto<GetDriverJoinLeaveResponseDto>> getDriverJoinLeave(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM")YearMonth from,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM")YearMonth to
    ) {
        ResponseDto<GetDriverJoinLeaveResponseDto> response = statsService.getDriverJoinLeave(from, to);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

}

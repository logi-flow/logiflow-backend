package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.common.mapper.PageMapper;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.PageDto;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.schedule.request.UpdateScheduleRequestDto;
import com.logi_flow.backend.dto.schedule.response.GetAllScheduleResponseDto;
import com.logi_flow.backend.dto.schedule.response.GetScheduleDetailResponseDto;
import com.logi_flow.backend.dto.schedule.response.UpdateScheduleResponseDto;
import com.logi_flow.backend.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "배차 스케줄 관리", description = "스케줄(Schedule) 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.SCHEDULE_API)
public class ScheduleController {
    private final ScheduleService scheduleService;

    private static final String SCHEDULE_ID_API = "/{scheduleId}";
    private static final String DRIVER_ID_API = "/drivers/{driverId}";

    @Operation(summary = "배차 스케줄 수정", description = "기사가 배송 정보, 상태를 입력하여 수정")
    @PutMapping(SCHEDULE_ID_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'ALLOCATIONS_MANAGER', 'DRIVER')")
    public ResponseEntity<ResponseDto<UpdateScheduleResponseDto>> updateSchedule(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long scheduleId, @Valid @RequestBody UpdateScheduleRequestDto dto) {
        ResponseDto<UpdateScheduleResponseDto> response = scheduleService.updateSchedule(userPrincipal, scheduleId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "배차 스케줄 전체 조회", description = "전체 배차 스케줄 목록 조회")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ALLOCATIONS_MANAGER')")
    public ResponseEntity<ResponseDto<PageDto<GetAllScheduleResponseDto>>> getAllSchedule(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {
        Page<GetAllScheduleResponseDto> result = scheduleService.getAllSchedule(page, size, sort);
        PageDto<GetAllScheduleResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "배차 스케줄 상세 조회", description = "특정 배차 스케줄 상세 조회")
    @GetMapping(SCHEDULE_ID_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'ALLOCATIONS_MANAGER', 'DRIVER')")
    public ResponseEntity<ResponseDto<GetScheduleDetailResponseDto>> getSchedule(@PathVariable Long scheduleId) {
        ResponseDto<GetScheduleDetailResponseDto> response = scheduleService.getSchedule(scheduleId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "특정 기사 배차 스케줄 전체 조회", description = "특정 기사 배차 스케줄 전체 조회")
    @GetMapping(DRIVER_ID_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'ALLOCATIONS_MANAGER')")
    public ResponseEntity<ResponseDto<PageDto<GetAllScheduleResponseDto>>> getScheduleByDriverId(
            @PathVariable Long driverId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {
        Page<GetAllScheduleResponseDto> result = scheduleService.getScheduleByDriverId(driverId, page, size, sort);
        PageDto<GetAllScheduleResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "기사 본인 배차 스케줄 전체 조회", description = "특정 기사 본인 배차 스케줄 전체 조회")
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('DRIVER')")
    public ResponseEntity<ResponseDto<PageDto<GetAllScheduleResponseDto>>> getMySchedules(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {
        Page<GetAllScheduleResponseDto> result = scheduleService.getMySchedules(userPrincipal, page, size, sort);
        PageDto<GetAllScheduleResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }


}

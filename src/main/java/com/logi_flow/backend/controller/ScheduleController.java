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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.SCHEDULE_API)
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PutMapping("/{scheduleId}")
    public ResponseEntity<ResponseDto<UpdateScheduleResponseDto>> updateSchedule(@PathVariable Long scheduleId, @Valid @RequestBody UpdateScheduleRequestDto dto) {
        ResponseDto<UpdateScheduleResponseDto> response = scheduleService.updateSchedule(scheduleId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<PageDto<GetAllScheduleResponseDto>>> getAllSchedule(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {
        Page<GetAllScheduleResponseDto> result = scheduleService.getAllSchedule(page, size, sort);
        PageDto<GetAllScheduleResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<ResponseDto<GetScheduleDetailResponseDto>> getSchedule(@PathVariable Long scheduleId) {
        ResponseDto<GetScheduleDetailResponseDto> response = scheduleService.getSchedule(scheduleId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping("/{driverId}")
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

    @GetMapping("/me")
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

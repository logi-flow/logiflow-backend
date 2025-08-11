package com.logi_flow.backend.controller;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.schedule.request.UpdateScheduleRequestDto;
import com.logi_flow.backend.dto.schedule.response.GetAllScheduleResponseDto;
import com.logi_flow.backend.dto.schedule.response.GetScheduleDetailResponseDto;
import com.logi_flow.backend.dto.schedule.response.UpdateScheduleResponseDto;
import com.logi_flow.backend.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/schedules")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PutMapping("/{scheduleId}")
    public ResponseEntity<ResponseDto<UpdateScheduleResponseDto>> updateSchedule(@PathVariable Long scheduleId, @RequestBody UpdateScheduleRequestDto dto) {
        ResponseDto<UpdateScheduleResponseDto> response = scheduleService.updateSchedule(scheduleId, dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<GetAllScheduleResponseDto>>> getAllSchedule() {
        ResponseDto<List<GetAllScheduleResponseDto>> response = scheduleService.getAllSchedule();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<ResponseDto<GetScheduleDetailResponseDto>> getSchedule(@PathVariable Long scheduleId) {
        ResponseDto<GetScheduleDetailResponseDto> response = scheduleService.getSchedule(scheduleId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{driverId}")
    public ResponseEntity<ResponseDto<List<GetAllScheduleResponseDto>>> getScheduleByDriverId(@PathVariable Long driverId) {
        ResponseDto<List<GetAllScheduleResponseDto>> response = scheduleService.getScheduleByDriverId(driverId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<ResponseDto<List<GetAllScheduleResponseDto>>> getMySchedules(@AuthenticationPrincipal UserPrincipal userPrincipal) {

        ResponseDto<List<GetAllScheduleResponseDto>> response = scheduleService.getMySchedules(userPrincipal);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}

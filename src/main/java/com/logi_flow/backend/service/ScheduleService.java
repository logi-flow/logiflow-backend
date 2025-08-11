package com.logi_flow.backend.service;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.schedule.request.UpdateScheduleRequestDto;
import com.logi_flow.backend.dto.schedule.response.GetAllScheduleResponseDto;
import com.logi_flow.backend.dto.schedule.response.GetScheduleDetailResponseDto;
import com.logi_flow.backend.dto.schedule.response.UpdateScheduleResponseDto;

import java.util.List;

public interface ScheduleService {
    ResponseDto<UpdateScheduleResponseDto> updateSchedule(Long scheduleId, UpdateScheduleRequestDto dto);

    ResponseDto<List<GetAllScheduleResponseDto>> getAllSchedule();

    ResponseDto<GetScheduleDetailResponseDto> getSchedule(Long scheduleId);

    ResponseDto<List<GetAllScheduleResponseDto>> getScheduleByDriverId(Long driverId);

    ResponseDto<List<GetAllScheduleResponseDto>> getMySchedules(UserPrincipal userPrincipal);
}

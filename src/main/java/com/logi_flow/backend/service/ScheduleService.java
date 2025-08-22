package com.logi_flow.backend.service;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.schedule.request.UpdateScheduleRequestDto;
import com.logi_flow.backend.dto.schedule.response.GetAllScheduleResponseDto;
import com.logi_flow.backend.dto.schedule.response.GetScheduleDetailResponseDto;
import com.logi_flow.backend.dto.schedule.response.UpdateScheduleResponseDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

public interface ScheduleService {

    ResponseDto<UpdateScheduleResponseDto> updateSchedule(UserPrincipal userPrincipal, Long scheduleId, @Valid UpdateScheduleRequestDto dto);

    Page<GetAllScheduleResponseDto> getAllSchedule(int page, int size, String sort);

    ResponseDto<GetScheduleDetailResponseDto> getSchedule(Long scheduleId);

    Page<GetAllScheduleResponseDto> getScheduleByDriverId(Long driverId, int page, int size, String sort);

    Page<GetAllScheduleResponseDto> getMySchedules(UserPrincipal userPrincipal, int page, int size, String sort);
}

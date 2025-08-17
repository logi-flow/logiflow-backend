package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.schedule.request.UpdateScheduleRequestDto;
import com.logi_flow.backend.dto.schedule.response.GetAllScheduleResponseDto;
import com.logi_flow.backend.dto.schedule.response.GetScheduleDetailResponseDto;
import com.logi_flow.backend.dto.schedule.response.UpdateScheduleResponseDto;
import com.logi_flow.backend.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Override
    public ResponseDto<UpdateScheduleResponseDto> updateSchedule(Long scheduleId, UpdateScheduleRequestDto dto) {
        return null;
    }

    @Override
    public Page<GetAllScheduleResponseDto> getAllSchedule(int page, int size, String sort) {
        return null;
    }

    @Override
    public ResponseDto<GetScheduleDetailResponseDto> getSchedule(Long scheduleId) {
        return null;
    }

    @Override
    public Page<GetAllScheduleResponseDto> getScheduleByDriverId(Long driverId, int page, int size, String sort) {
        return null;
    }

    @Override
    public Page<GetAllScheduleResponseDto> getMySchedules(UserPrincipal userPrincipal, int page, int size, String sort) {
        return null;
    }
}

package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.constants.ResponseMessage;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.schedule.request.UpdateScheduleRequestDto;
import com.logi_flow.backend.dto.schedule.response.GetAllScheduleResponseDto;
import com.logi_flow.backend.dto.schedule.response.GetScheduleDetailResponseDto;
import com.logi_flow.backend.dto.schedule.response.UpdateScheduleResponseDto;
import com.logi_flow.backend.entity.Allocation;
import com.logi_flow.backend.entity.Schedule;
import com.logi_flow.backend.repository.AllocationRepository;
import com.logi_flow.backend.repository.ScheduleRepository;
import com.logi_flow.backend.service.ScheduleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final AllocationRepository allocationRepository;
    // 스케쥴은 배차 만들면 자동생성인가

    @Override
    public ResponseDto<UpdateScheduleResponseDto> updateSchedule(Long scheduleId, UpdateScheduleRequestDto dto) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        Allocation findAllocation = allocationRepository.findById(dto.getAllocationId()).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        if(!findAllocation.getId().equals(schedule.getAllocation().getId())) {
            schedule.setAllocation(findAllocation);
        }

        if(!dto.getAllocationDate().equals(schedule.getAllocationDate())) {
            schedule.setAllocationDate(schedule.getAllocationDate());
        }

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

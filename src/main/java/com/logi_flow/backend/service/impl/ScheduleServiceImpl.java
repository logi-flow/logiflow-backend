package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.constants.ResponseCode;
import com.logi_flow.backend.common.constants.ResponseMessage;
import com.logi_flow.backend.common.enums.AllocationStatus;
import com.logi_flow.backend.common.enums.user.UserRole;
import com.logi_flow.backend.common.util.DateUtils;
import com.logi_flow.backend.common.util.SortUtils;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.schedule.request.UpdateScheduleRequestDto;
import com.logi_flow.backend.dto.schedule.response.GetAllScheduleResponseDto;
import com.logi_flow.backend.dto.schedule.response.GetScheduleDetailResponseDto;
import com.logi_flow.backend.dto.schedule.response.UpdateScheduleResponseDto;
import com.logi_flow.backend.entity.*;
import com.logi_flow.backend.repository.*;
import com.logi_flow.backend.service.ScheduleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;
    private final AllocationRepository allocationRepository;
    private final AllocationStatusLogRepository allocationStatusLogRepository;
    private final DriverRepository driverRepository;

    @Override
    @Transactional
    public ResponseDto<UpdateScheduleResponseDto> updateSchedule(UserPrincipal userPrincipal, Long scheduleId, UpdateScheduleRequestDto dto) {
        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        if(!user.getRole().getName().equals(UserRole.DRIVER)) {
            return ResponseDto.fail("FORBIDDEN", ResponseMessage.NO_PERMISSION);
        }

        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));
        Allocation allocation = schedule.getAllocation();
        AllocationStatus prevStatus = allocation.getStatus();

        if(dto.getStatus() != allocation.getStatus()) {
            allocation.setStatus(dto.getStatus());
        }

        Allocation updatedAllocation = allocationRepository.save(allocation);

        AllocationStatusLog allocationStatusLog = AllocationStatusLog.builder()
                .allocation(allocation)
                .user(user)
                .changedByUsername(username)
                .changeReason(dto.getChangeReason())
                .prevStatus(prevStatus)
                .newStatus(updatedAllocation.getStatus())
                .build();

        allocationStatusLogRepository.save(allocationStatusLog);


        if(updatedAllocation.getStatus().equals(AllocationStatus.IN_PROGRESS)) {

            if(schedule.getDepartureTime() == null) {
                schedule.setDepartureTime(dto.getDepartureTime());
            }

            if(schedule.getArrivalTime() == null) {
                schedule.setArrivalTime(dto.getArrivalTime());
            }

            scheduleRepository.save(schedule);
        }

        UpdateScheduleResponseDto data = UpdateScheduleResponseDto.builder()
                .id(schedule.getId())
                .allocationId(schedule.getAllocation().getId())
                .allocationDate(schedule.getAllocationDate())
                .departureTime(DateUtils.format(schedule.getDepartureTime()))
                .arrivalTime(DateUtils.format(schedule.getArrivalTime()))
                .createdAt(DateUtils.format(schedule.getCreatedAt()))
                .updatedAt(DateUtils.format(schedule.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public Page<GetAllScheduleResponseDto> getAllSchedule(int page, int size, String sort) {
        Page<GetAllScheduleResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<Schedule> schedules = scheduleRepository.findAll(pageable);

        data = schedules.map(this::toGetAllScheduleResponseDto);

        return data;
    }

    @Override
    public ResponseDto<GetScheduleDetailResponseDto> getSchedule(Long scheduleId) {

        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        GetScheduleDetailResponseDto data = GetScheduleDetailResponseDto.builder()
                .id(schedule.getId())
                .allocationId(schedule.getAllocation().getId())
                .allocationDate(schedule.getAllocationDate())
                .departureTime(DateUtils.format(schedule.getDepartureTime()))
                .arrivalTime(DateUtils.format(schedule.getArrivalTime()))
                .createdAt(DateUtils.format(schedule.getCreatedAt()))
                .updatedAt(DateUtils.format(schedule.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public Page<GetAllScheduleResponseDto> getScheduleByDriverId(Long driverId, int page, int size, String sort) {
        Page<GetAllScheduleResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<Schedule> schedules = scheduleRepository.findByDriverId(driverId, pageable);

        data = schedules.map(this::toGetAllScheduleResponseDto);
        return data;
    }

    @Override
    public Page<GetAllScheduleResponseDto> getMySchedules(UserPrincipal userPrincipal, int page, int size, String sort) {
        // 기사 시점 본인 스케줄
        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));
        Driver driver = driverRepository.findByUser(user).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Page<GetAllScheduleResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<Schedule> schedules = scheduleRepository.findByDriverId(driver.getId(), pageable);

        data = schedules.map(this::toGetAllScheduleResponseDto);
        return data;
    }

    private GetAllScheduleResponseDto toGetAllScheduleResponseDto(Schedule schedule) {
        return GetAllScheduleResponseDto.builder()
                .id(schedule.getId())
                .allocationId(schedule.getAllocation().getId())
                .allocationDate(schedule.getAllocationDate())
                .departureTime(DateUtils.format(schedule.getDepartureTime()))
                .arrivalTime(DateUtils.format(schedule.getArrivalTime()))
                .createdAt(DateUtils.format(schedule.getCreatedAt()))
                .updatedAt(DateUtils.format(schedule.getUpdatedAt()))
                .build();
    }
}

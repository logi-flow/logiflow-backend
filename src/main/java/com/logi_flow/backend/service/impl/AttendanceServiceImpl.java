package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.constants.ResponseCode;
import com.logi_flow.backend.common.constants.ResponseMessage;
import com.logi_flow.backend.common.enums.AssignmentStatus;
import com.logi_flow.backend.common.util.DateUtils;
import com.logi_flow.backend.common.util.SortUtils;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.attendance.request.UpdateAttendanceRequestDto;
import com.logi_flow.backend.dto.attendance.response.*;
import com.logi_flow.backend.entity.Assignment;
import com.logi_flow.backend.entity.Attendance;
import com.logi_flow.backend.entity.Driver;
import com.logi_flow.backend.repository.AssignmentRepository;
import com.logi_flow.backend.repository.AttendanceRepository;
import com.logi_flow.backend.repository.DriverRepository;
import com.logi_flow.backend.service.AttendanceService;
import com.logi_flow.backend.service.VehicleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final DriverRepository driverRepository;
    private final AssignmentRepository assignmentRepository;
    private final VehicleService vehicleService;

    @Override
    @Transactional
    public ResponseDto<CreateAttendanceResponseDto> checkInAttendance(UserPrincipal userPrincipal) {
        CreateAttendanceResponseDto data = null;

        Driver driver = driverRepository.findByUserId(userPrincipal.getId())
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Attendance newAttendance = Attendance.builder()
                .driver(driver)
                .workStart(LocalDateTime.now())
                .build();

        Attendance savedAttendance = attendanceRepository.save(newAttendance);

        data = CreateAttendanceResponseDto.builder()
                .id(savedAttendance.getId())
                .driverId(savedAttendance.getDriver().getId())
                .workStart(DateUtils.format(savedAttendance.getWorkStart()))
                .openFlag(savedAttendance.getOpenFlag())
                .createdAt(DateUtils.format(savedAttendance.getCreatedAt()))
                .updatedAt(DateUtils.format(savedAttendance.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    @Transactional
    public ResponseDto<UpdateAttendanceResponseDto> checkOutAttendance(UserPrincipal userPrincipal, UpdateAttendanceRequestDto dto) {
        UpdateAttendanceResponseDto data = null;

        Driver driver = driverRepository.findByUserId(userPrincipal.getId())
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Assignment assignment = assignmentRepository.findByDriverIdAndStatus(driver.getId(), AssignmentStatus.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        BigDecimal updatedVehicleMileageMileage = vehicleService.updateVehicleMileage(userPrincipal, assignment.getVehicle().getId(), dto.getVehicleMileage());

        Attendance attendance = attendanceRepository.findOpenAttendanceForUpdate(driver.getId())
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.NO_OPEN_ATTENDANCE));

        attendance.setWorkEnd(LocalDateTime.now());
        Attendance savedAttendance = attendanceRepository.save(attendance);

        data = UpdateAttendanceResponseDto.builder()
                .id(savedAttendance.getId())
                .driverId(savedAttendance.getDriver().getId())
                .workStart(DateUtils.format(savedAttendance.getWorkStart()))
                .workEnd(DateUtils.format(savedAttendance.getWorkEnd()))
                .openFlag(savedAttendance.getOpenFlag())
                .vehicleMileage(updatedVehicleMileageMileage)
                .createdAt(DateUtils.format(savedAttendance.getCreatedAt()))
                .updatedAt(DateUtils.format(savedAttendance.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public Page<GetAllAttendanceResponseDto> getAllAttendance(int page, int size, String sort) {
        Page<GetAllAttendanceResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<Attendance> attendances = attendanceRepository.findAll(pageable);

        data = attendances.map(this::toGetAllAttendanceResponseDto);

        return data;
    }

    @Override
    public ResponseDto<GetAttendanceDetailResponseDto> getAttendanceDetails(Long attendanceId) {
        GetAttendanceDetailResponseDto data = null;

        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        data = GetAttendanceDetailResponseDto.builder()
                .id(attendance.getId())
                .driverId(attendance.getDriver().getId())
                .driverName(attendance.getDriver().getName())
                .driverPhone(attendance.getDriver().getPhoneNumber())
                .driverCompanyJoin(attendance.getDriver().getCompanyJoin())
                .workStart(DateUtils.format(attendance.getWorkStart()))
                .workEnd(DateUtils.format(attendance.getWorkEnd()))
                .openFlag(attendance.getOpenFlag())
                .createdAt(DateUtils.format(attendance.getCreatedAt()))
                .updatedAt(DateUtils.format(attendance.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<GetMyAttendanceDetailResponseDto> getMyAttendance(UserPrincipal userPrincipal) {
        GetMyAttendanceDetailResponseDto data = null;

        Driver driver = driverRepository.findByUserId(userPrincipal.getId())
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Optional<Attendance> attendance = attendanceRepository.findOpenAttendance(driver.getId());

        if (attendance.isPresent()) {
            Attendance openAttendance = attendance.get();

            data = GetMyAttendanceDetailResponseDto.builder()
                    .isOpen(true)
                    .workStart(DateUtils.format(openAttendance.getWorkStart()))
                    .workEnd(openAttendance.getWorkEnd() == null ? null : DateUtils.format(openAttendance.getWorkEnd()))
                    .createdAt(DateUtils.format(openAttendance.getCreatedAt()))
                    .updatedAt(DateUtils.format(openAttendance.getUpdatedAt()))
                    .build();
        } else {
            data = GetMyAttendanceDetailResponseDto.builder()
                    .isOpen(false)
                    .workStart(null)
                    .workEnd(null)
                    .createdAt(null)
                    .updatedAt(null)
                    .build();
        }

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public Page<GetAllMyAttendanceResponseDto> getAllMyAttendance(UserPrincipal userPrincipal, int page, int size, String sort) {
        Page<GetAllMyAttendanceResponseDto> data = null;

        Driver driver = driverRepository.findByUserId(userPrincipal.getId())
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<Attendance> attendances = attendanceRepository.findByDriverId(driver.getId(), pageable);

        data = attendances.map(this::toGetMyAttendanceResponseDto);

        return data;
    }

    private GetAllAttendanceResponseDto toGetAllAttendanceResponseDto(Attendance attendance) {
        return GetAllAttendanceResponseDto.builder()
                .driverId(attendance.getDriver().getId())
                .driverName(attendance.getDriver().getName())
                .workStart(DateUtils.format(attendance.getWorkStart()))
                .workEnd(DateUtils.format(attendance.getWorkEnd()))
                .createdAt(DateUtils.format(attendance.getCreatedAt()))
                .updatedAt(DateUtils.format(attendance.getUpdatedAt()))
                .build();
    }

    private GetAllMyAttendanceResponseDto toGetMyAttendanceResponseDto(Attendance attendance) {
        return GetAllMyAttendanceResponseDto.builder()
                .workStart(DateUtils.format(attendance.getWorkStart()))
                .workEnd(DateUtils.format(attendance.getWorkEnd()))
                .createdAt(DateUtils.format(attendance.getCreatedAt()))
                .updatedAt(DateUtils.format(attendance.getUpdatedAt()))
                .build();
    }
}

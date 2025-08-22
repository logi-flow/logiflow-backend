package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.constants.ResponseCode;
import com.logi_flow.backend.common.constants.ResponseMessage;
import com.logi_flow.backend.common.enums.AssignmentStatus;
import com.logi_flow.backend.common.enums.driver.VehicleStatus;
import com.logi_flow.backend.common.util.SortUtils;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.assignment.request.CreateAssignmentRequestDto;
import com.logi_flow.backend.dto.assignment.request.UpdateAssignmentRequestDto;
import com.logi_flow.backend.dto.assignment.request.UpdateAssignmentStatusRequestDto;
import com.logi_flow.backend.dto.assignment.response.CreateAssignmentResponseDto;
import com.logi_flow.backend.dto.assignment.response.GetAllAssignmentResponseDto;
import com.logi_flow.backend.dto.assignment.response.GetAssignmentDetailResponseDto;
import com.logi_flow.backend.dto.assignment.response.UpdateAssignmentResponseDto;
import com.logi_flow.backend.entity.*;
import com.logi_flow.backend.repository.*;
import com.logi_flow.backend.service.AssignmentService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final AssignmentStatusLogRepository assignmentStatusLogRepository;
    private final VehicleStatusLogRepository vehicleStatusLogRepository;

    @Override
    @Transactional
    public ResponseDto<CreateAssignmentResponseDto> createAssignment(CreateAssignmentRequestDto dto) {
        CreateAssignmentResponseDto data = null;

        Driver driver = driverRepository.findById(dto.getDriverId())
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId())
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        if (vehicle.getStatus() != VehicleStatus.AVAILABLE) {
            throw new IllegalArgumentException(ResponseMessage.FAILED);
        }

        if (assignmentRepository.existsByVehicleAndStatus(vehicle, AssignmentStatus.ACTIVE)) {
            throw new IllegalArgumentException(ResponseMessage.FAILED);
        }

        Optional<Assignment> pausedAssignmentOpt = assignmentRepository.findByVehicleAndStatus(vehicle, AssignmentStatus.PAUSED);
        if (pausedAssignmentOpt.isPresent()) {
            Assignment pausedAssignment = pausedAssignmentOpt.get();
            if (!pausedAssignment.getDriver().getId().equals(dto.getDriverId())) {
                throw new IllegalArgumentException(ResponseMessage.ALREADY_EXISTS);
            }
        }

        if (dto.getIsPrimary() != null && dto.getIsPrimary()) {
            assignmentRepository.findByDriverAndIsPrimaryFalseAndStatus(driver, AssignmentStatus.ACTIVE)
                    .forEach(assignment -> {
                        assignment.setStatus(AssignmentStatus.DELETED);
                        assignment.getVehicle().setStatus(VehicleStatus.AVAILABLE);
                    });
        }

        Assignment newAssignment = Assignment.builder()
                .driver(driver)
                .vehicle(vehicle)
                .isPrimary(dto.getIsPrimary())
                .status(AssignmentStatus.ACTIVE)
                .build();

        vehicle.setStatus(VehicleStatus.IN_USE);
        assignmentRepository.save(newAssignment);

        data = CreateAssignmentResponseDto.builder()
                .id(newAssignment.getId())
                .driverId(newAssignment.getDriver().getId())
                .driverName(newAssignment.getDriver().getName())
                .vehicleIds(newAssignment.getVehicle().getId())
                .isPrimary(newAssignment.isPrimary())
                .status(newAssignment.getStatus())
                .createdAt(newAssignment.getCreatedAt())
                .updatedAt(newAssignment.getUpdatedAt())
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<UpdateAssignmentResponseDto> updateAssignment(Long assignmentId, @Valid UpdateAssignmentRequestDto dto) {
        UpdateAssignmentResponseDto data = null;

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        if (dto.getIsPrimary() != null && assignment.isPrimary() != dto.getIsPrimary()) {
            if (dto.getIsPrimary()) {
                Driver driver = assignment.getDriver();
                assignmentRepository.findByDriverAndIsPrimaryTrueAndStatus(driver, AssignmentStatus.ACTIVE)
                        .ifPresent(currentPrimary -> currentPrimary.setPrimary(false));
            }
            assignment.setPrimary(dto.getIsPrimary());
        }

        assignmentRepository.save(assignment);

        data = UpdateAssignmentResponseDto.builder()
                .id(assignment.getId())
                .driverId(assignment.getDriver().getId())
                .vehicleId(assignment.getVehicle().getId())
                .isPrimary(assignment.isPrimary())
                .status(assignment.getStatus())
                .createdAt(assignment.getCreatedAt())
                .updatedAt(assignment.getUpdatedAt())
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<UpdateAssignmentResponseDto> updateAssignmentStatus(UserPrincipal userPrincipal, Long assignmentId, UpdateAssignmentStatusRequestDto dto) {
        UpdateAssignmentResponseDto data = null;

        User user = userRepository.findByUsername(userPrincipal.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        AssignmentStatus prevStatus = assignment.getStatus();

        if (dto.getStatus() != null && !assignment.getStatus().equals(dto.getStatus())) {
            assignment.setStatus(dto.getStatus());
        }

        assignmentRepository.save(assignment);

        AssignmentStatus newStatus = assignment.getStatus();

        AssignmentStatusLog assignmentUpdateLog = AssignmentStatusLog.builder()
                .assignment(assignment)
                .user(user)
                .changedByUsername(user.getUsername())
                .changeReason(dto.getChangeReason())
                .prevStatus(prevStatus)
                .newStatus(newStatus)
                .build();

        assignmentStatusLogRepository.save(assignmentUpdateLog);

        data = UpdateAssignmentResponseDto.builder()
                .id(assignment.getId())
                .driverId(assignment.getDriver().getId())
                .vehicleId(assignment.getVehicle().getId())
                .isPrimary(assignment.isPrimary())
                .status(assignment.getStatus())
                .createdAt(assignment.getCreatedAt())
                .updatedAt(assignment.getUpdatedAt())
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public Page<GetAllAssignmentResponseDto> getAllAssignment(int page, int size, String sort) {
        Page<GetAllAssignmentResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<Assignment> assignments = assignmentRepository.findAll(pageable);

        data = assignments.map(this::toGetAllAssignmentResponseDto);

        return data;
    }

    @Override
    public ResponseDto<GetAssignmentDetailResponseDto> getAssignmentDetail(Long assignmentId) {
        GetAssignmentDetailResponseDto data = null;

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        data = GetAssignmentDetailResponseDto.builder()
                .id(assignment.getId())
                .driverId(assignment.getDriver().getId())
                .vehicleIds(assignment.getVehicle().getId())
                .isPrimary(assignment.isPrimary())
                .status(assignment.getStatus())
                .createdAt(assignment.getCreatedAt())
                .updatedAt(assignment.getUpdatedAt())
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<?> deleteAssignment(Long assignmentId) {
        return null;
    }

    @Override
    @Transactional
    public void pauseAssignment(Vehicle vehicle) {
        assignmentRepository.findByVehicleAndStatus(vehicle, AssignmentStatus.ACTIVE)
                .ifPresent(assignment -> {
                    AssignmentStatus prevStatus = assignment.getStatus();
                    assignment.setStatus(AssignmentStatus.PAUSED);
                    AssignmentStatus newStatus = assignment.getStatus();

                    Vehicle subVehicle = vehicleRepository.findFirstByStatus(VehicleStatus.AVAILABLE)
                            .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.RESOURCE_NOT_FOUND));

                    Assignment subAssignment = Assignment.builder()
                            .driver(assignment.getDriver())
                            .vehicle(subVehicle)
                            .status(AssignmentStatus.ACTIVE)
                            .isPrimary(false)
                            .build();

                    assignmentRepository.save(subAssignment);


                    String name = "SYSTEM";
                    String reason = "차량 수리에 의한 중지";

                    AssignmentStatusLog assignmentUpdateLog = AssignmentStatusLog.builder()
                            .assignment(assignment)
                            .user(null)
                            .changedByUsername(name)
                            .changeReason(reason)
                            .prevStatus(prevStatus)
                            .newStatus(newStatus)
                            .build();

                    assignmentStatusLogRepository.save(assignmentUpdateLog);

                    subVehicle.setStatus(VehicleStatus.IN_USE);

                    String vehicleReason = "임시차량 배정";

                    VehicleStatusLog vehicleStatusLog = VehicleStatusLog.builder()
                            .vehicle(subVehicle)
                            .user(null)
                            .changedByUsername(name)
                            .changeReason(vehicleReason)
                            .prevStatus(VehicleStatus.AVAILABLE)
                            .newStatus(subVehicle.getStatus())
                            .build();

                    vehicleStatusLogRepository.save(vehicleStatusLog);
                });
    }

    @Override
    @Transactional
    public void removeAssignmentByVehicle(Vehicle vehicle) {
        assignmentRepository.findActiveOrPausedByVehicle(vehicle)
                .ifPresent(assignment -> {
                    AssignmentStatus prevStatus = assignment.getStatus();
                    assignment.setStatus(AssignmentStatus.DELETED);
                    AssignmentStatus newStatus = assignment.getStatus();

                    assignmentRepository.save(assignment);

                    String name = "SYSTEM";
                    String reason = "차량 수리에 의한 삭제";

                    AssignmentStatusLog assignmentUpdateLog = AssignmentStatusLog.builder()
                            .assignment(assignment)
                            .user(null)
                            .changedByUsername(name)
                            .changeReason(reason)
                            .prevStatus(prevStatus)
                            .newStatus(newStatus)
                            .build();

                    assignmentStatusLogRepository.save(assignmentUpdateLog);
                });
    }

    private GetAllAssignmentResponseDto toGetAllAssignmentResponseDto(Assignment assignment) {
        return GetAllAssignmentResponseDto.builder()
                .id(assignment.getId())
                .driverId(assignment.getDriver().getId())
                .vehicleId(assignment.getVehicle().getId())
                .isPrimary(assignment.isPrimary())
                .status(assignment.getStatus())
                .createdAt(assignment.getCreatedAt())
                .updatedAt(assignment.getUpdatedAt())
                .build();
    }
}

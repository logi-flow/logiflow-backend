package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.constants.ResponseCode;
import com.logi_flow.backend.common.constants.ResponseMessage;
import com.logi_flow.backend.common.enums.AssignmentStatus;
import com.logi_flow.backend.common.enums.driver.VehicleStatus;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.assignment.request.CreateAssignmentRequestDto;
import com.logi_flow.backend.dto.assignment.request.UpdateAssignmentRequestDto;
import com.logi_flow.backend.dto.assignment.response.CreateAssignmentResponseDto;
import com.logi_flow.backend.dto.assignment.response.GetAllAssignmentResponseDto;
import com.logi_flow.backend.dto.assignment.response.GetAssignmentDetailResponseDto;
import com.logi_flow.backend.dto.assignment.response.UpdateAssignmentResponseDto;
import com.logi_flow.backend.entity.Assignment;
import com.logi_flow.backend.entity.Driver;
import com.logi_flow.backend.entity.Vehicle;
import com.logi_flow.backend.repository.AssignmentRepository;
import com.logi_flow.backend.repository.DriverRepository;
import com.logi_flow.backend.repository.VehicleRepository;
import com.logi_flow.backend.service.AssignmentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;

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
    public ResponseDto<UpdateAssignmentResponseDto> updateAssignment(Long assignmentId, UpdateAssignmentRequestDto dto) {
        UpdateAssignmentResponseDto data = null;

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        if (dto.getStatus() != null && !assignment.getStatus().equals(dto.getStatus())) {
            assignment.setStatus(dto.getStatus());
        }

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
    public ResponseDto<List<GetAllAssignmentResponseDto>> getAllAssignment() {
        List<GetAllAssignmentResponseDto> data = null;

        List<Assignment> assignments = assignmentRepository.findAll();

        data = assignments.stream()
                .map(assignment -> GetAllAssignmentResponseDto.builder()
                        .id(assignment.getId())
                        .driverId(assignment.getDriver().getId())
                        .vehicleId(assignment.getVehicle().getId())
                        .status(assignment.getStatus())
                        .isPrimary(assignment.isPrimary())
                        .createdAt(assignment.getCreatedAt())
                        .updatedAt(assignment.getUpdatedAt())
                        .build()
                ).collect(Collectors.toList());

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
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
                    assignment.setStatus(AssignmentStatus.PAUSED);

                    Vehicle subVehicle = vehicleRepository.findFirstByStatus(VehicleStatus.AVAILABLE)
                            .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.RESOURCE_NOT_FOUND));

                    Assignment subAssignment = Assignment.builder()
                            .driver(assignment.getDriver())
                            .vehicle(subVehicle)
                            .status(AssignmentStatus.ACTIVE)
                            .isPrimary(false)
                            .build();

                    assignmentRepository.save(subAssignment);
                    subVehicle.setStatus(VehicleStatus.IN_USE);
                });
    }

    @Override
    public void removeAssignmentByVehicle(Vehicle vehicle) {
        assignmentRepository.findActiveOrPausedByVehicle(vehicle)
                .ifPresent(assignment ->
                        assignment.setStatus(AssignmentStatus.DELETED)
                );
    }
}

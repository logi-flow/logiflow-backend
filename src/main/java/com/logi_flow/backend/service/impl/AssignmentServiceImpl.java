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

import java.util.List;

@RequiredArgsConstructor
@Service
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;

    @Override
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
        return null;
    }

    @Override
    public ResponseDto<List<GetAllAssignmentResponseDto>> getAllAssignment() {
        return null;
    }

    @Override
    public ResponseDto<GetAssignmentDetailResponseDto> getAssignmentDetail(Long assignmentId) {
        return null;
    }

    @Override
    public ResponseDto<?> deleteAssignment(Long assignmentId) {
        return null;
    }
}

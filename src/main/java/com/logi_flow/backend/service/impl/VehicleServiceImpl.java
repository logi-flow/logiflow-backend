package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.constants.ResponseCode;
import com.logi_flow.backend.common.constants.ResponseMessage;
import com.logi_flow.backend.common.enums.driver.VehicleStatus;
import com.logi_flow.backend.common.util.DateUtils;
import com.logi_flow.backend.common.util.SortUtils;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.vehicle.request.CreateVehicleRequestDto;
import com.logi_flow.backend.dto.vehicle.request.UpdateVehicleRequestDto;
import com.logi_flow.backend.dto.vehicle.request.UpdateVehicleStatusRequestDto;
import com.logi_flow.backend.dto.vehicle.response.CreateVehicleResponseDto;
import com.logi_flow.backend.dto.vehicle.response.GetAllVehicleResponseDto;
import com.logi_flow.backend.dto.vehicle.response.GetVehicleDetailResponseDto;
import com.logi_flow.backend.dto.vehicle.response.UpdateVehicleResponseDto;
import com.logi_flow.backend.entity.User;
import com.logi_flow.backend.entity.Vehicle;
import com.logi_flow.backend.entity.VehicleStatusLog;
import com.logi_flow.backend.repository.UserRepository;
import com.logi_flow.backend.repository.VehicleRepository;
import com.logi_flow.backend.repository.VehicleStatusLogRepository;
import com.logi_flow.backend.service.AssignmentService;
import com.logi_flow.backend.service.VehicleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final AssignmentService assignmentService;
    private final UserRepository userRepository;
    private final VehicleStatusLogRepository vehicleStatusLogRepository;

    @Override
    public ResponseDto<CreateVehicleResponseDto> createVehicle(CreateVehicleRequestDto dto) {
        CreateVehicleResponseDto data = null;

        Vehicle newVehicle = Vehicle.builder()
                .vehicleNumber(dto.getVehicleNumber())
                .capacity(dto.getCapacity())
                .fuel(dto.getFuel())
                .mileage(dto.getMileage())
                .status(dto.getStatus())
                .modelName(dto.getModelName())
                .modelYear(dto.getModelYear())
                .build();

        vehicleRepository.save(newVehicle);

        data = CreateVehicleResponseDto.builder()
                .vehicleId(newVehicle.getId())
                .vehicleNumber(newVehicle.getVehicleNumber())
                .status(newVehicle.getStatus())
                .createdAt(DateUtils.format(newVehicle.getCreatedAt()))
                .updatedAt(DateUtils.format(newVehicle.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<UpdateVehicleResponseDto> updateVehicle(Long vehicleId, UpdateVehicleRequestDto dto) {
        UpdateVehicleResponseDto data = null;

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));


        if (dto.getVehicleNumber() != null && !vehicle.getVehicleNumber().equals(dto.getVehicleNumber())) {
            vehicle.setVehicleNumber(dto.getVehicleNumber());
        }
        if (dto.getCapacity() != null && !dto.getCapacity().equals(vehicle.getCapacity())) {
            vehicle.setCapacity(dto.getCapacity());
        }
        if (dto.getFuel() != null && !vehicle.getFuel().equals(dto.getFuel())) {
            vehicle.setFuel(dto.getFuel());
        }
        if (dto.getMileage() != null && !vehicle.getMileage().equals(dto.getMileage())) {
            vehicle.setMileage(dto.getMileage());
        }
        if (dto.getModelName() != null && !vehicle.getModelName().equals(dto.getModelName())) {
            vehicle.setModelName(dto.getModelName());
        }
        if (dto.getModelYear() != null && !vehicle.getModelYear().equals(dto.getModelYear())) {
            vehicle.setModelYear(dto.getModelYear());
        }

        Vehicle updateVehicle = vehicleRepository.save(vehicle);

        data = UpdateVehicleResponseDto.builder()
                .vehicleId(updateVehicle.getId())
                .vehicleNumber(updateVehicle.getVehicleNumber())
                .status(updateVehicle.getStatus())
                .createdAt(DateUtils.format(updateVehicle.getCreatedAt()))
                .updatedAt(DateUtils.format(updateVehicle.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<UpdateVehicleResponseDto> updateVehicleStatus(UserPrincipal userPrincipal, Long vehicleId, UpdateVehicleStatusRequestDto dto) {
        UpdateVehicleResponseDto data = null;

        User user = userRepository.findByUsername(userPrincipal.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        VehicleStatus prevStatus = vehicle.getStatus();

        if (dto.getStatus() != null && !vehicle.getStatus().equals(dto.getStatus())) {
            vehicle.setStatus(dto.getStatus());
        }

        VehicleStatus newStatus = vehicle.getStatus();

        if (prevStatus != newStatus) {
            if (newStatus == VehicleStatus.UNDER_MAINTENANCE) {
                assignmentService.pauseAssignment(vehicle);
            }
            else if (newStatus == VehicleStatus.DELETED) {
                assignmentService.removeAssignmentByVehicle(vehicle);
            }
        }

        Vehicle updateVehicle = vehicleRepository.save(vehicle);

        VehicleStatusLog vehicleStatusLog = VehicleStatusLog.builder()
                .vehicle(vehicle)
                .user(user)
                .changedByUsername(user.getUsername())
                .changeReason(dto.getChangeReason())
                .prevStatus(prevStatus)
                .newStatus(newStatus)
                .build();

        vehicleStatusLogRepository.save(vehicleStatusLog);

        data = UpdateVehicleResponseDto.builder()
                .vehicleId(updateVehicle.getId())
                .vehicleNumber(updateVehicle.getVehicleNumber())
                .status(updateVehicle.getStatus())
                .createdAt(DateUtils.format(updateVehicle.getCreatedAt()))
                .updatedAt(DateUtils.format(updateVehicle.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GetAllVehicleResponseDto> getAllVehicle(int page, int size, String sort) {
        Page<GetAllVehicleResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<Vehicle> vehicles = vehicleRepository.findAll(pageable);

        data = vehicles.map(this::toGetAllVehicleResponseDto);

        return data;
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseDto<GetVehicleDetailResponseDto> getVehicleDetail(Long vehicleId) {
        GetVehicleDetailResponseDto data = null;

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        data = GetVehicleDetailResponseDto.builder()
                .vehicleId(vehicle.getId())
                .vehicleNumber(vehicle.getVehicleNumber())
                .capacity(vehicle.getCapacity())
                .fuel(vehicle.getFuel())
                .mileage(vehicle.getMileage())
                .status(vehicle.getStatus())
                .modelName(vehicle.getModelName())
                .modelYear(vehicle.getModelYear())
                .createdAt(DateUtils.format(vehicle.getCreatedAt()))
                .updatedAt(DateUtils.format(vehicle.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<?> delete(Long vehicleId) {
        return null;
    }

    private GetAllVehicleResponseDto toGetAllVehicleResponseDto(Vehicle vehicle) {
        return GetAllVehicleResponseDto.builder()
                .vehicleId(vehicle.getId())
                .vehicleNumber(vehicle.getVehicleNumber())
                .status(vehicle.getStatus())
                .modelName(vehicle.getModelName())
                .createdAt(DateUtils.format(vehicle.getCreatedAt()))
                .updatedAt(DateUtils.format(vehicle.getUpdatedAt()))
                .build();
    }
}

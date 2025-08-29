package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.constants.ResponseCode;
import com.logi_flow.backend.common.constants.ResponseMessage;
import com.logi_flow.backend.common.enums.TableRef;
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
import com.logi_flow.backend.entity.VehicleUpdateLog;
import com.logi_flow.backend.repository.UserRepository;
import com.logi_flow.backend.repository.VehicleRepository;
import com.logi_flow.backend.repository.VehicleStatusLogRepository;
import com.logi_flow.backend.repository.VehicleUpdateLogRepository;
import com.logi_flow.backend.service.AssignmentService;
import com.logi_flow.backend.service.DeleteLogService;
import com.logi_flow.backend.service.VehicleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final AssignmentService assignmentService;
    private final UserRepository userRepository;
    private final VehicleStatusLogRepository vehicleStatusLogRepository;
    private final VehicleUpdateLogRepository vehicleUpdateLogRepository;
    private final DeleteLogService deleteLogService;

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
    public ResponseDto<UpdateVehicleResponseDto> updateVehicle(UserPrincipal userPrincipal, Long vehicleId, UpdateVehicleRequestDto dto) {
        UpdateVehicleResponseDto data = null;

        User user = userRepository.findByUsername(userPrincipal.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        List<VehicleUpdateLog> logs = new ArrayList<>();

        if (dto.getVehicleNumber() != null && !vehicle.getVehicleNumber().equals(dto.getVehicleNumber())) {
            String prevData = vehicle.getVehicleNumber();
            logs.add(buildUpdateLog(vehicle, user, "vehicle_number", prevData, dto.getVehicleNumber()));
            vehicle.setVehicleNumber(dto.getVehicleNumber());
        }
        if (dto.getCapacity() != null && !dto.getCapacity().equals(vehicle.getCapacity())) {
            String prevData = String.valueOf(vehicle.getCapacity());
            logs.add(buildUpdateLog(vehicle, user, "capacity", prevData, String.valueOf(dto.getCapacity())));
            vehicle.setCapacity(dto.getCapacity());
        }
        if (dto.getFuel() != null && !vehicle.getFuel().equals(dto.getFuel())) {
            String prevData = String.valueOf(vehicle.getFuel());
            logs.add(buildUpdateLog(vehicle, user, "fuel", prevData, String.valueOf(dto.getFuel())));
            vehicle.setFuel(dto.getFuel());
        }
        if (dto.getMileage() != null && !vehicle.getMileage().equals(dto.getMileage())) {
            String prevData = String.valueOf(vehicle.getMileage());
            logs.add(buildUpdateLog(vehicle, user, "mileage", prevData, String.valueOf(dto.getMileage())));
            vehicle.setMileage(dto.getMileage());
        }
        if (dto.getModelName() != null && !vehicle.getModelName().equals(dto.getModelName())) {
            String prevData = vehicle.getModelName();
            logs.add(buildUpdateLog(vehicle, user, "model_name", prevData, dto.getModelName()));
            vehicle.setModelName(dto.getModelName());
        }
        if (dto.getModelYear() != null && !vehicle.getModelYear().equals(dto.getModelYear())) {
            String prevData = String.valueOf(vehicle.getModelYear());
            logs.add(buildUpdateLog(vehicle, user, "model_year", prevData, String.valueOf(dto.getModelYear())));
            vehicle.setModelYear(dto.getModelYear());
        }

        Vehicle updateVehicle = vehicleRepository.save(vehicle);
        vehicleUpdateLogRepository.saveAll(logs);

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
    public BigDecimal updateVehicleMileage(UserPrincipal userPrincipal, Long vehicleId, BigDecimal mileage) {
        User user = userRepository.findByUsername(userPrincipal.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        VehicleUpdateLog log = null;

        if (mileage != null && !vehicle.getMileage().equals(mileage)) {
            log = buildUpdateLog(vehicle, user, "mileage", String.valueOf(vehicle.getMileage()), String.valueOf(mileage));
            vehicle.setMileage(mileage);
        }

        vehicleRepository.save(vehicle);

        if (log != null) {
            vehicleUpdateLogRepository.save(log);
        }
        return vehicle.getMileage();
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
    public ResponseDto<Void> deleteVehicle(UserPrincipal userPrincipal, Long vehicleId) {
        User user = userRepository.findByUsername(userPrincipal.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        if (vehicle.getStatus() == VehicleStatus.DELETED) {
            return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        }

        vehicle.setStatus(VehicleStatus.DELETED);
        vehicleRepository.save(vehicle);

        deleteLogService.createLog(TableRef.VEHICLE, vehicleId, user);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
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

    private VehicleUpdateLog buildUpdateLog(Vehicle vehicle, User user, String type, String prevData, String newData) {
        return VehicleUpdateLog.builder()
                .vehicle(vehicle)
                .user(user)
                .changedByUsername(user.getUsername())
                .type(type)
                .prevData(prevData)
                .newData(newData)
                .build();
    }
}

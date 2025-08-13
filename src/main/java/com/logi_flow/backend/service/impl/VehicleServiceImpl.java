package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.constants.ResponseCode;
import com.logi_flow.backend.common.constants.ResponseMessage;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.vehicle.request.CreateVehicleRequestDto;
import com.logi_flow.backend.dto.vehicle.request.UpdateVehicleRequestDto;
import com.logi_flow.backend.dto.vehicle.response.CreateVehicleResponseDto;
import com.logi_flow.backend.dto.vehicle.response.GetAllVehicleResponseDto;
import com.logi_flow.backend.dto.vehicle.response.GetVehicleDetailResponseDto;
import com.logi_flow.backend.dto.vehicle.response.UpdateVehicleResponseDto;
import com.logi_flow.backend.entity.Vehicle;
import com.logi_flow.backend.repository.VehicleRepository;
import com.logi_flow.backend.service.VehicleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;

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
                .createdAt(newVehicle.getCreatedAt())
                .updatedAt(newVehicle.getUpdatedAt())
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<UpdateVehicleResponseDto> updateVehicle(Long vehicleId, UpdateVehicleRequestDto dto) {
        UpdateVehicleResponseDto data = null;

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        if (dto.getVehicleNumber() != null && !vehicle.getVehicleNumber().equals(dto.getVehicleNumber()))
            vehicle.setVehicleNumber(dto.getVehicleNumber());
        if (dto.getCapacity() != null && !dto.getCapacity().equals(vehicle.getCapacity()))
            vehicle.setCapacity(dto.getCapacity());
        if (dto.getFuel() != null && !vehicle.getFuel().equals(dto.getFuel()))
            vehicle.setFuel(dto.getFuel());
        if (dto.getMileage() != null && !vehicle.getMileage().equals(dto.getMileage()))
            vehicle.setMileage(dto.getMileage());
        if (dto.getStatus() != null && !vehicle.getStatus().equals(dto.getStatus()))
            vehicle.setStatus(dto.getStatus());
        if (dto.getModelName() != null && !vehicle.getModelName().equals(dto.getModelName()))
            vehicle.setModelName(dto.getModelName());
        if (dto.getModelYear() != null && !vehicle.getModelYear().equals(dto.getModelYear()))
            vehicle.setModelYear(dto.getModelYear());

        Vehicle updateVehicle = vehicleRepository.save(vehicle);

        data = UpdateVehicleResponseDto.builder()
                .vehicleId(updateVehicle.getId())
                .vehicleNumber(updateVehicle.getVehicleNumber())
                .status(updateVehicle.getStatus())
                .createdAt(updateVehicle.getCreatedAt())
                .updatedAt(updateVehicle.getUpdatedAt())
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<List<GetAllVehicleResponseDto>> getAllVehicle() {
        List<GetAllVehicleResponseDto> data = null;

        List<Vehicle> vehicles = vehicleRepository.findAll();

        data = vehicles.stream()
                .map(vehicle -> GetAllVehicleResponseDto.builder()
                        .vehicleId(vehicle.getId())
                        .vehicleNumber(vehicle.getVehicleNumber())
                        .status(vehicle.getStatus())
                        .modelName(vehicle.getModelName())
                        .createdAt(vehicle.getCreatedAt())
                        .updatedAt(vehicle.getUpdatedAt())
                        .build()).collect(Collectors.toList());

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
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
                .createdAt(vehicle.getCreatedAt())
                .updatedAt(vehicle.getUpdatedAt())
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<?> delete(Long vehicleId) {
        return null;
    }
}

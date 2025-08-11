package com.logi_flow.backend.service;

import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.vehicle.request.CreateVehicleRequestDto;
import com.logi_flow.backend.dto.vehicle.request.UpdateVehicleRequestDto;
import com.logi_flow.backend.dto.vehicle.response.CreateVehicleResponseDto;
import com.logi_flow.backend.dto.vehicle.response.GetAllVehicleResponseDto;
import com.logi_flow.backend.dto.vehicle.response.GetVehicleDetailResponseDto;
import com.logi_flow.backend.dto.vehicle.response.UpdateVehicleResponseDto;

public interface VehicleService {
    ResponseDto<CreateVehicleResponseDto> createVehicle(CreateVehicleRequestDto dto);

    ResponseDto<UpdateVehicleResponseDto> updateVehicle(Long vehicleId, UpdateVehicleRequestDto dto);

    ResponseDto<GetAllVehicleResponseDto> getAllVehicle();

    ResponseDto<GetVehicleDetailResponseDto> getVehicleDetail(Long vehicleId);

    ResponseDto<?> delete(Long vehicleId);
}

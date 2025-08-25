package com.logi_flow.backend.service;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.vehicle.request.CreateVehicleRequestDto;
import com.logi_flow.backend.dto.vehicle.request.UpdateVehicleRequestDto;
import com.logi_flow.backend.dto.vehicle.request.UpdateVehicleStatusRequestDto;
import com.logi_flow.backend.dto.vehicle.response.CreateVehicleResponseDto;
import com.logi_flow.backend.dto.vehicle.response.GetAllVehicleResponseDto;
import com.logi_flow.backend.dto.vehicle.response.GetVehicleDetailResponseDto;
import com.logi_flow.backend.dto.vehicle.response.UpdateVehicleResponseDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

public interface VehicleService {
    ResponseDto<CreateVehicleResponseDto> createVehicle(@Valid CreateVehicleRequestDto dto);

    ResponseDto<UpdateVehicleResponseDto> updateVehicle(UserPrincipal userPrincipal, Long vehicleId, @Valid UpdateVehicleRequestDto dto);

    Page<GetAllVehicleResponseDto> getAllVehicle(int page, int size, String sort);

    ResponseDto<GetVehicleDetailResponseDto> getVehicleDetail(Long vehicleId);

    ResponseDto<Void> deleteVehicle(UserPrincipal userPrincipal, Long vehicleId);

    ResponseDto<UpdateVehicleResponseDto> updateVehicleStatus(UserPrincipal userPrincipal, Long vehicleId, @Valid UpdateVehicleStatusRequestDto dto);
}

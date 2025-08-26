package com.logi_flow.backend.service;

import com.logi_flow.backend.dto.vehicle.response.VehicleStatusLogResponseDto;
import com.logi_flow.backend.dto.vehicle.response.VehicleUpdateLogResponseDto;
import org.springframework.data.domain.Page;

public interface VehicleLogService {
    Page<VehicleStatusLogResponseDto> getStatusLog(int page, int size, String sort);

    Page<VehicleUpdateLogResponseDto> getUpdateLog(int page, int size, String sort);
}

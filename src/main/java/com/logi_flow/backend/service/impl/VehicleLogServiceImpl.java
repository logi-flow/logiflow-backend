package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.util.DateUtils;
import com.logi_flow.backend.common.util.SortUtils;
import com.logi_flow.backend.dto.vehicle.response.VehicleStatusLogResponseDto;
import com.logi_flow.backend.dto.vehicle.response.VehicleUpdateLogResponseDto;
import com.logi_flow.backend.entity.VehicleStatusLog;
import com.logi_flow.backend.entity.VehicleUpdateLog;
import com.logi_flow.backend.repository.VehicleStatusLogRepository;
import com.logi_flow.backend.repository.VehicleUpdateLogRepository;
import com.logi_flow.backend.service.VehicleLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
@Transactional
public class VehicleLogServiceImpl implements VehicleLogService {

    private final VehicleStatusLogRepository vehicleStatusLogRepository;
    private final VehicleUpdateLogRepository vehicleUpdateLogRepository;

    @Override
    public Page<VehicleStatusLogResponseDto> getStatusLog(int page, int size, String sort) {
        Page<VehicleStatusLogResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<VehicleStatusLog> vehicleStatusLogs = vehicleStatusLogRepository.findAll(pageable);

        data = vehicleStatusLogs.map(this::toVehicleStatusLogResponseDto);

        return data;
    }

    @Override
    public Page<VehicleUpdateLogResponseDto> getUpdateLog(int page, int size, String sort) {
        Page<VehicleUpdateLogResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<VehicleUpdateLog> vehicleUpdateLogs = vehicleUpdateLogRepository.findAll(pageable);

        data = vehicleUpdateLogs.map(this::toVehicleUpdateLogResponseDto);

        return data;
    }

    private VehicleStatusLogResponseDto toVehicleStatusLogResponseDto(VehicleStatusLog vehicleStatusLog) {
        return VehicleStatusLogResponseDto.builder()
                .id(vehicleStatusLog.getId())
                .vehicleId(vehicleStatusLog.getVehicle().getId())
                .changedByUsername(vehicleStatusLog.getChangedByUsername())
                .changeReason(vehicleStatusLog.getChangeReason())
                .prevStatus(vehicleStatusLog.getPrevStatus())
                .newStatus(vehicleStatusLog.getNewStatus())
                .createdAt(DateUtils.format(vehicleStatusLog.getCreatedAt()))
                .build();
    }

    private VehicleUpdateLogResponseDto toVehicleUpdateLogResponseDto(VehicleUpdateLog vehicleUpdateLog) {
        return VehicleUpdateLogResponseDto.builder()
                .id(vehicleUpdateLog.getId())
                .vehicleId(vehicleUpdateLog.getVehicle().getId())
                .changedByUsername(vehicleUpdateLog.getChangedByUsername())
                .type(vehicleUpdateLog.getType())
                .prevData(vehicleUpdateLog.getPrevData())
                .newData(vehicleUpdateLog.getNewData())
                .createdAt(DateUtils.format(vehicleUpdateLog.getCreatedAt()))
                .build();
    }
}
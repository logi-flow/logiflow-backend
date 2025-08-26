package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.util.DateUtils;
import com.logi_flow.backend.common.util.SortUtils;
import com.logi_flow.backend.dto.allocationLog.response.GetAllocationStatusLogResponseDto;
import com.logi_flow.backend.dto.allocationLog.response.GetAllocationUpdateLogResponseDto;
import com.logi_flow.backend.entity.AllocationStatusLog;
import com.logi_flow.backend.entity.AllocationUpdateLog;
import com.logi_flow.backend.repository.AllocationStatusLogRepository;
import com.logi_flow.backend.repository.AllocationUpdateLogRepository;
import com.logi_flow.backend.service.AllocationLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AllocationLogServiceImpl implements AllocationLogService {

    private final AllocationUpdateLogRepository allocationLogRepository;
    private final AllocationStatusLogRepository allocationStatusLogRepository;

    @Override
    public Page<GetAllocationUpdateLogResponseDto> getAllocationUpdateLog(int page, int size, String sort) {

        Page<GetAllocationUpdateLogResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<AllocationUpdateLog> allocationUpdateLogs = allocationLogRepository.findAll(pageable);

        data = allocationUpdateLogs.map(this::toGetAllocationUpdateLogResponseDto);
        return data;
    }

    @Override
    public Page<GetAllocationStatusLogResponseDto> getAllocationStatusLog(int page, int size, String sort) {

        Page<GetAllocationStatusLogResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<AllocationStatusLog> allocationStatusLogs = allocationStatusLogRepository.findAll(pageable);

        data = allocationStatusLogs.map(this::toGetAllocationStatusLogResponseDto);
        return data;
    }

    private GetAllocationUpdateLogResponseDto toGetAllocationUpdateLogResponseDto(AllocationUpdateLog allocationUpdateLog) {
        return GetAllocationUpdateLogResponseDto.builder()
                .deliveryId(allocationUpdateLog.getAllocation().getDelivery().getId())
                .returnDeliveryId(allocationUpdateLog.getAllocation().getReturnDelivery().getId())
                .driverName(allocationUpdateLog.getAllocation().getAssignment().getDriver().getName())
                .vehicleNumber(allocationUpdateLog.getAllocation().getAssignment().getVehicle().getVehicleNumber())
                .changedByUsername(allocationUpdateLog.getChangedByUsername())
                .type(allocationUpdateLog.getType())
                .prevData(allocationUpdateLog.getPrevData())
                .newData(allocationUpdateLog.getNewData())
                .createdAt(DateUtils.format(allocationUpdateLog.getCreatedAt()))
                .build();
    }

    private GetAllocationStatusLogResponseDto toGetAllocationStatusLogResponseDto(AllocationStatusLog allocationStatusLog) {
        return GetAllocationStatusLogResponseDto.builder()
                .deliveryId(allocationStatusLog.getAllocation().getDelivery().getId())
                .returnDeliveryId(allocationStatusLog.getAllocation().getReturnDelivery().getId())
                .driverName(allocationStatusLog.getAllocation().getAssignment().getDriver().getName())
                .vehicleNumber(allocationStatusLog.getAllocation().getAssignment().getVehicle().getVehicleNumber())
                .changedByUsername(allocationStatusLog.getChangedByUsername())
                .changeReason(allocationStatusLog.getChangeReason())
                .prevStatus(allocationStatusLog.getPrevStatus())
                .newStatus(allocationStatusLog.getNewStatus())
                .createdAt(DateUtils.format(allocationStatusLog.getCreatedAt()))
                .build();
    }
}

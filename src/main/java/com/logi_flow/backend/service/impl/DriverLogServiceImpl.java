package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.util.DateUtils;
import com.logi_flow.backend.common.util.SortUtils;
import com.logi_flow.backend.dto.driver.response.DriverStatusLogResponseDto;
import com.logi_flow.backend.dto.driver.response.DriverUpdateLogResponseDto;
import com.logi_flow.backend.entity.DriverStatusLog;
import com.logi_flow.backend.entity.DriverUpdateLog;
import com.logi_flow.backend.repository.DriverStatusLogRepository;
import com.logi_flow.backend.repository.DriverUpdateLogRepository;
import com.logi_flow.backend.service.DriverLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class DriverLogServiceImpl implements DriverLogService {

    private final DriverStatusLogRepository driverStatusLogRepository;
    private final DriverUpdateLogRepository driverUpdateLogRepository;

    @Override
    public Page<DriverStatusLogResponseDto> getStatusLog(int page, int size, String sort) {
        Page<DriverStatusLogResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<DriverStatusLog> driverStatusLogs = driverStatusLogRepository.findAll(pageable);

        data = driverStatusLogs.map(this::toDriverStatusLogResponseDto);

        return data;
    }

    @Override
    public Page<DriverUpdateLogResponseDto> getUpdateLog(int page, int size, String sort) {
        Page<DriverUpdateLogResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<DriverUpdateLog> driverUpdateLogs = driverUpdateLogRepository.findAll(pageable);

        data = driverUpdateLogs.map(this::toDriverUpdateLogResponseDto);

        return data;
    }

    private DriverStatusLogResponseDto toDriverStatusLogResponseDto(DriverStatusLog driverStatusLog) {
        return DriverStatusLogResponseDto.builder()
                .id(driverStatusLog.getId())
                .driverId(driverStatusLog.getDriver().getId())
                .changedByUsername(driverStatusLog.getChangedByUsername())
                .changeReason(driverStatusLog.getChangeReason())
                .prevStatus(driverStatusLog.getPrevStatus())
                .newStatus(driverStatusLog.getNewStatus())
                .createdAt(DateUtils.format(driverStatusLog.getCreatedAt()))
                .build();
    }

    private DriverUpdateLogResponseDto toDriverUpdateLogResponseDto(DriverUpdateLog driverUpdateLog) {
        return DriverUpdateLogResponseDto.builder()
                .id(driverUpdateLog.getId())
                .driverId(driverUpdateLog.getDriver().getId())
                .changedByUsername(driverUpdateLog.getChangedByUsername())
                .type(driverUpdateLog.getType())
                .prevData(driverUpdateLog.getPrevData())
                .newData(driverUpdateLog.getNewData())
                .createdAt(DateUtils.format(driverUpdateLog.getCreatedAt()))
                .build();
    }
}

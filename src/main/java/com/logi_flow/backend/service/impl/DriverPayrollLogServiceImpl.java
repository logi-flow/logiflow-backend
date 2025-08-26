package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.util.DateUtils;
import com.logi_flow.backend.common.util.SortUtils;
import com.logi_flow.backend.dto.driverPayrollLog.response.GetDriverPayrollStatusLogResponseDto;
import com.logi_flow.backend.dto.driverPayrollLog.response.GetDriverPayrollUpdateLogResponseDto;
import com.logi_flow.backend.entity.DriverPayrollStatusLog;
import com.logi_flow.backend.entity.DriverPayrollUpdateLog;
import com.logi_flow.backend.repository.DriverPayrollStatusLogRepository;
import com.logi_flow.backend.repository.DriverPayrollUpdateLogRepository;
import com.logi_flow.backend.service.DriverPayrollLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DriverPayrollLogServiceImpl implements DriverPayrollLogService {

    private final DriverPayrollUpdateLogRepository driverPayrollUpdateLogRepository;
    private final DriverPayrollStatusLogRepository driverPayrollStatusLogRepository;

    @Override
    public Page<GetDriverPayrollUpdateLogResponseDto> getDriverPayrollUpdateLogs(int page, int size, String sort) {
        Page<GetDriverPayrollUpdateLogResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<DriverPayrollUpdateLog> logs = driverPayrollUpdateLogRepository.findAll(pageable);

        data = logs.map(this::toGetDriverPayrollUpdateLogResponseDto);

        return data;
    }

    @Override
    public Page<GetDriverPayrollStatusLogResponseDto> getDriverPayrollStatusLogs(int page, int size, String sort) {
        Page<GetDriverPayrollStatusLogResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<DriverPayrollStatusLog> logs = driverPayrollStatusLogRepository.findAll(pageable);

        data = logs.map(this::toGetDriverPayrollStatusLogResponseDto);

        return data;
    }

    private GetDriverPayrollUpdateLogResponseDto toGetDriverPayrollUpdateLogResponseDto(DriverPayrollUpdateLog driverPayrollUpdateLog) {
        return GetDriverPayrollUpdateLogResponseDto.builder()
                .id(driverPayrollUpdateLog.getId())
                .driverId(driverPayrollUpdateLog.getDriverPayroll().getDriver().getId())
                .driverName(driverPayrollUpdateLog.getDriverPayroll().getDriver().getName())
                .payrollId(driverPayrollUpdateLog.getDriverPayroll().getId())
                .type(driverPayrollUpdateLog.getType())
                .prevData(driverPayrollUpdateLog.getPrevData())
                .newData(driverPayrollUpdateLog.getNewData())
                .changedByUsername(driverPayrollUpdateLog.getChangedByUsername())
                .createdAt(DateUtils.format(driverPayrollUpdateLog.getCreatedAt()))
                .build();
    }

    private GetDriverPayrollStatusLogResponseDto toGetDriverPayrollStatusLogResponseDto(DriverPayrollStatusLog driverPayrollStatusLog) {
        return GetDriverPayrollStatusLogResponseDto.builder()
                .id(driverPayrollStatusLog.getId())
                .driverId(driverPayrollStatusLog.getDriverPayroll().getDriver().getId())
                .driverName(driverPayrollStatusLog.getDriverPayroll().getDriver().getName())
                .payrollId(driverPayrollStatusLog.getDriverPayroll().getId())
                .prevStatus(driverPayrollStatusLog.getPrevStatus())
                .newStatus(driverPayrollStatusLog.getNewStatus())
                .changedByUsername(driverPayrollStatusLog.getChangedByUsername())
                .changeReason(driverPayrollStatusLog.getChangeReason())
                .createdAt(DateUtils.format(driverPayrollStatusLog.getCreatedAt()))
                .build();
    }
}

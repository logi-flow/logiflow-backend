package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.util.DateUtils;
import com.logi_flow.backend.common.util.SortUtils;
import com.logi_flow.backend.dto.driverAllowanceLog.response.GetDriverAllowanceUpdateLogResponseDto;
import com.logi_flow.backend.entity.DriverAllowanceUpdateLog;
import com.logi_flow.backend.repository.DriverAllowanceUpdateLogRepository;
import com.logi_flow.backend.service.DriverAllowanceLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DriverAllowanceLogServiceImpl implements DriverAllowanceLogService {

    private final DriverAllowanceUpdateLogRepository driverAllowanceUpdateLogRepository;

    @Override
    public Page<GetDriverAllowanceUpdateLogResponseDto> getDriverAllowanceUpdateLogs(int page, int size, String sort) {
        Page<GetDriverAllowanceUpdateLogResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<DriverAllowanceUpdateLog> logs = driverAllowanceUpdateLogRepository.findAll(pageable);

        data = logs.map(this::toGetDriverAllowanceUpdateLogResponseDto);

        return data;
    }

    private GetDriverAllowanceUpdateLogResponseDto toGetDriverAllowanceUpdateLogResponseDto(DriverAllowanceUpdateLog driverAllowanceUpdateLog) {
        return GetDriverAllowanceUpdateLogResponseDto.builder()
                .id(driverAllowanceUpdateLog.getId())
                .driverId(driverAllowanceUpdateLog.getDriverAllowance().getDriverPayroll().getDriver().getId())
                .driverName(driverAllowanceUpdateLog.getDriverAllowance().getDriverPayroll().getDriver().getName())
                .payrollId(driverAllowanceUpdateLog.getDriverAllowance().getDriverPayroll().getId())
                .code(driverAllowanceUpdateLog.getDriverAllowance().getAllowanceType().getCode())
                .name(driverAllowanceUpdateLog.getDriverAllowance().getAllowanceType().getName())
                .type(driverAllowanceUpdateLog.getType())
                .prevData(driverAllowanceUpdateLog.getPrevData())
                .newData(driverAllowanceUpdateLog.getNewData())
                .changedByUsername(driverAllowanceUpdateLog.getChangedByUsername())
                .createdAt(DateUtils.format(driverAllowanceUpdateLog.getCreatedAt()))
                .build();
    }
}

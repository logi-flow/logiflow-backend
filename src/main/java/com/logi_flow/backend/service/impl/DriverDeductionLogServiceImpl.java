package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.util.DateUtils;
import com.logi_flow.backend.common.util.SortUtils;
import com.logi_flow.backend.dto.driverDeductionLog.response.GetDriverDeductionUpdateLogResponseDto;
import com.logi_flow.backend.entity.DriverDeductionUpdateLog;
import com.logi_flow.backend.repository.DriverDeductionUpdateLogRepository;
import com.logi_flow.backend.service.DriverDeductionLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DriverDeductionLogServiceImpl implements DriverDeductionLogService {

    private final DriverDeductionUpdateLogRepository driverDeductionUpdateLogRepository;

    @Override
    public Page<GetDriverDeductionUpdateLogResponseDto> getDriverDeductionUpdateLogs(int page, int size, String sort) {
        Page<GetDriverDeductionUpdateLogResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<DriverDeductionUpdateLog> logs = driverDeductionUpdateLogRepository.findAll(pageable);

        data = logs.map(this::toGetDriverDeductionUpdateLogResponseDto);

        return data;
    }

    private GetDriverDeductionUpdateLogResponseDto toGetDriverDeductionUpdateLogResponseDto(DriverDeductionUpdateLog driverDeductionUpdateLog) {
        return GetDriverDeductionUpdateLogResponseDto.builder()
                .id(driverDeductionUpdateLog.getId())
                .driverId(driverDeductionUpdateLog.getDriverDeduction().getDriverPayroll().getDriver().getId())
                .driverName(driverDeductionUpdateLog.getDriverDeduction().getDriverPayroll().getDriver().getName())
                .payrollId(driverDeductionUpdateLog.getDriverDeduction().getDriverPayroll().getId())
                .code(driverDeductionUpdateLog.getDriverDeduction().getDeductionType().getCode())
                .name(driverDeductionUpdateLog.getDriverDeduction().getDeductionType().getName())
                .type(driverDeductionUpdateLog.getType())
                .prevData(driverDeductionUpdateLog.getPrevData())
                .newData(driverDeductionUpdateLog.getNewData())
                .changedByUsername(driverDeductionUpdateLog.getChangedByUsername())
                .createdAt(DateUtils.format(driverDeductionUpdateLog.getCreatedAt()))
                .build();
    }
}

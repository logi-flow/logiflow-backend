package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.util.DateUtils;
import com.logi_flow.backend.common.util.SortUtils;
import com.logi_flow.backend.dto.driver.response.DriverLicenseLogResponseDto;
import com.logi_flow.backend.entity.DriverLicenseLog;
import com.logi_flow.backend.repository.DriverLicenseLogRepository;
import com.logi_flow.backend.service.DriverLicenseLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class DriverLicenseLogServiceImpl implements DriverLicenseLogService {

    private final DriverLicenseLogRepository driverLicenseLogRepository;

    @Override
    public Page<DriverLicenseLogResponseDto> getUpdateLog(int page, int size, String sort) {
        Page<DriverLicenseLogResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<DriverLicenseLog> driverLicenseLogs = driverLicenseLogRepository.findAll(pageable);

        data = driverLicenseLogs.map(this::toDriverLicenseLogResponseDto);

        return data;
    }

    private DriverLicenseLogResponseDto toDriverLicenseLogResponseDto(DriverLicenseLog driverLicenseLog) {
        return DriverLicenseLogResponseDto.builder()
                .id(driverLicenseLog.getId())
                .licenseId(driverLicenseLog.getDriverLicense().getId())
                .driverName(driverLicenseLog.getDriverLicense().getDriver().getName())
                .changedByUsername(driverLicenseLog.getChangedByUsername())
                .type(driverLicenseLog.getType())
                .prevData(driverLicenseLog.getPrevData())
                .newData(driverLicenseLog.getNewData())
                .createdAt(DateUtils.format(driverLicenseLog.getCreatedAt()))
                .build();
    }
}
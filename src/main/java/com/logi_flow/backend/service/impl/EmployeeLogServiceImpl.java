package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.util.DateUtils;
import com.logi_flow.backend.common.util.SortUtils;
import com.logi_flow.backend.dto.customerLog.response.GetCustomerStatusLogResponseDto;
import com.logi_flow.backend.dto.employeeLog.response.GetEmployeeStatusLogResponseDto;
import com.logi_flow.backend.dto.employeeLog.response.GetEmployeeUpdateLogResponseDto;
import com.logi_flow.backend.entity.CustomerStatusLog;
import com.logi_flow.backend.entity.EmployeeStatusLog;
import com.logi_flow.backend.entity.EmployeeUpdateLog;
import com.logi_flow.backend.repository.EmployeeStatusLogRepository;
import com.logi_flow.backend.repository.EmployeeUpdateLogRepository;
import com.logi_flow.backend.service.EmployeeLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmployeeLogServiceImpl implements EmployeeLogService {
    private final EmployeeUpdateLogRepository employeeUpdateLogRepository;
    private final EmployeeStatusLogRepository employeeStatusLogRepository;

    @Override
    public Page<GetEmployeeUpdateLogResponseDto> getEmployeeUpdateLogs(int page, int size, String sort) {
        Page<GetEmployeeUpdateLogResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<EmployeeUpdateLog> employeeUpdateLogs = employeeUpdateLogRepository.findAll(pageable);

        data = employeeUpdateLogs.map(this::toGetEmployeeUpdateLogResponseDto);
        return data;
    }

    @Override
    public Page<GetEmployeeStatusLogResponseDto> getEmployeeStatusLogs(int page, int size, String sort) {
        Page<GetEmployeeStatusLogResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<EmployeeStatusLog> employeeStatusLogs = employeeStatusLogRepository.findAll(pageable);

        data = employeeStatusLogs.map(this::toGetEmployeeStatusLogResponseDto);
        return data;
    }

    private GetEmployeeUpdateLogResponseDto toGetEmployeeUpdateLogResponseDto (EmployeeUpdateLog employeeUpdateLog) {
        return GetEmployeeUpdateLogResponseDto.builder()
                .id(employeeUpdateLog.getId())
                .employeeId(employeeUpdateLog.getEmployee().getId())
                .username(employeeUpdateLog.getChangedByUsername())
                .type(employeeUpdateLog.getType())
                .prevData(employeeUpdateLog.getPrevData())
                .newData(employeeUpdateLog.getNewData())
                .createdAt(DateUtils.format(employeeUpdateLog.getCreatedAt()))
                .build();
    }

    private GetEmployeeStatusLogResponseDto toGetEmployeeStatusLogResponseDto (EmployeeStatusLog employeeStatusLog) {
        return GetEmployeeStatusLogResponseDto.builder()
                .id(employeeStatusLog.getId())
                .employeeId(employeeStatusLog.getEmployee().getId())
                .username(employeeStatusLog.getChangedByUsername())
                .changeReason(employeeStatusLog.getChangeReason())
                .prevStatus(employeeStatusLog.getPrevStatus())
                .newStatus(employeeStatusLog.getNewStatus())
                .createdAt(DateUtils.format(employeeStatusLog.getCreatedAt()))
                .build();
    }

    private GetCustomerStatusLogResponseDto toGetCustomerStatusLogResponseDto (CustomerStatusLog customerStatusLog) {
        return GetCustomerStatusLogResponseDto.builder()
                .id(customerStatusLog.getId())
                .customerId(customerStatusLog.getCustomer().getId())
                .username(customerStatusLog.getChangedByUsername())
                .changeReason(customerStatusLog.getChangeReason())
                .prevStatus(customerStatusLog.getPrevStatus())
                .newStatus(customerStatusLog.getNewStatus())
                .createdAt(DateUtils.format(customerStatusLog.getCreatedAt()))
                .build();
    }
}

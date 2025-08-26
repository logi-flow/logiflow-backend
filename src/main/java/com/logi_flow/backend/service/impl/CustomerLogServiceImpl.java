package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.util.DateUtils;
import com.logi_flow.backend.common.util.SortUtils;
import com.logi_flow.backend.dto.customerLog.response.GetCustomerStatusLogResponseDto;
import com.logi_flow.backend.dto.customerLog.response.GetCustomerUpdateLogResponseDto;
import com.logi_flow.backend.entity.CustomerStatusLog;
import com.logi_flow.backend.entity.CustomerUpdateLog;
import com.logi_flow.backend.repository.CustomerStatusLogRepository;
import com.logi_flow.backend.repository.CustomerUpdateLogRepository;
import com.logi_flow.backend.service.CustomerLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomerLogServiceImpl implements CustomerLogService {
    private final CustomerUpdateLogRepository customerUpdateLogRepository;
    private final CustomerStatusLogRepository customerStatusLogRepository;

    @Override
    public Page<GetCustomerUpdateLogResponseDto> getCustomerUpdateLogs(int page, int size, String sort) {
        Page<GetCustomerUpdateLogResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<CustomerUpdateLog> customerUpdateLogs = customerUpdateLogRepository.findAll(pageable);

        data = customerUpdateLogs.map(this::toGetCustomerUpdateLogResponseDto);
        return data;
    }

    @Override
    public Page<GetCustomerStatusLogResponseDto> getCustomerStatusLogs(int page, int size, String sort) {
        Page<GetCustomerStatusLogResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<CustomerStatusLog> customerStatusLogs = customerStatusLogRepository.findAll(pageable);

        data = customerStatusLogs.map(this::toGetCustomerStatusLogResponseDto);
        return data;
    }

    private GetCustomerUpdateLogResponseDto toGetCustomerUpdateLogResponseDto (CustomerUpdateLog customerUpdateLog) {
        return GetCustomerUpdateLogResponseDto.builder()
                .id(customerUpdateLog.getId())
                .customerId(customerUpdateLog.getCustomer().getId())
                .username(customerUpdateLog.getChangedByUsername())
                .type(customerUpdateLog.getType())
                .prevData(customerUpdateLog.getPrevData())
                .newData(customerUpdateLog.getNewData())
                .createdAt(DateUtils.format(customerUpdateLog.getCreatedAt()))
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

package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.util.DateUtils;
import com.logi_flow.backend.common.util.SortUtils;
import com.logi_flow.backend.dto.allowanceTypeLog.response.GetAllowanceTypeUpdateLogResponseDto;
import com.logi_flow.backend.entity.AllowanceTypeUpdateLog;
import com.logi_flow.backend.repository.AllowanceTypeUpdateLogRepository;
import com.logi_flow.backend.service.AllowanceTypeLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AllowanceTypeLogServiceImpl implements AllowanceTypeLogService {

    private final AllowanceTypeUpdateLogRepository allowanceTypeUpdateLogRepository;

    @Override
    public Page<GetAllowanceTypeUpdateLogResponseDto> getAllowanceTypeUpdateLogs(int page, int size, String sort) {
        Page<GetAllowanceTypeUpdateLogResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<AllowanceTypeUpdateLog> logs = allowanceTypeUpdateLogRepository.findAll(pageable);

        data = logs.map(this::toGetAllowanceTypeUpdateLogResponseDto);

        return data;
    }

    private GetAllowanceTypeUpdateLogResponseDto toGetAllowanceTypeUpdateLogResponseDto(AllowanceTypeUpdateLog allowanceTypeUpdateLog) {
        return GetAllowanceTypeUpdateLogResponseDto.builder()
                .id(allowanceTypeUpdateLog.getId())
                .code(allowanceTypeUpdateLog.getAllowanceType().getCode())
                .type(allowanceTypeUpdateLog.getType())
                .prevData(allowanceTypeUpdateLog.getPrevData())
                .newData(allowanceTypeUpdateLog.getNewData())
                .changedByUsername(allowanceTypeUpdateLog.getChangedByUsername())
                .createdAt(DateUtils.format(allowanceTypeUpdateLog.getCreatedAt()))
                .build();
    }
}

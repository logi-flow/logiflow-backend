package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.util.DateUtils;
import com.logi_flow.backend.common.util.SortUtils;
import com.logi_flow.backend.dto.deductionTypeLog.response.GetDeductionTypeUpdateLogResponseDto;
import com.logi_flow.backend.entity.DeductionTypeUpdateLog;
import com.logi_flow.backend.repository.DeductionTypeUpdateLogRepository;
import com.logi_flow.backend.service.DeductionTypeLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeductionTypeLogServiceImpl implements DeductionTypeLogService {

    private final DeductionTypeUpdateLogRepository deductionTypeUpdateLogRepository;

    @Override
    public Page<GetDeductionTypeUpdateLogResponseDto> getDeductionTypeUpdateLogs(int page, int size, String sort) {
        Page<GetDeductionTypeUpdateLogResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<DeductionTypeUpdateLog> logs = deductionTypeUpdateLogRepository.findAll(pageable);

        data = logs.map(this::toGetDeductionTypeUpdateLogResponseDto);

        return data;
    }

    private GetDeductionTypeUpdateLogResponseDto toGetDeductionTypeUpdateLogResponseDto(DeductionTypeUpdateLog deductionTypeUpdateLog) {
        return GetDeductionTypeUpdateLogResponseDto.builder()
                .id(deductionTypeUpdateLog.getId())
                .code(deductionTypeUpdateLog.getDeductionType().getCode())
                .type(deductionTypeUpdateLog.getType())
                .prevData(deductionTypeUpdateLog.getPrevData())
                .newData(deductionTypeUpdateLog.getNewData())
                .changedByUsername(deductionTypeUpdateLog.getChangedByUsername())
                .createdAt(DateUtils.format(deductionTypeUpdateLog.getCreatedAt()))
                .build();
    }
}

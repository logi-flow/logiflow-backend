package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.util.DateUtils;
import com.logi_flow.backend.common.util.SortUtils;
import com.logi_flow.backend.dto.retunDeliveryLog.response.GetAllReturnDeliveryStatusLogResponseDto;
import com.logi_flow.backend.dto.retunDeliveryLog.response.GetAllReturnDeliveryUpdateLogResponseDto;
import com.logi_flow.backend.entity.ReturnDeliveryStatusLog;
import com.logi_flow.backend.entity.ReturnDeliveryUpdateLog;
import com.logi_flow.backend.repository.ReturnDeliveryStatusLogRepository;
import com.logi_flow.backend.repository.ReturnDeliveryUpdateLogRepository;
import com.logi_flow.backend.service.ReturnDeliveryLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReturnDeliveryLogServiceImpl implements ReturnDeliveryLogService {
    private final ReturnDeliveryStatusLogRepository returnDeliveryStatusLogRepository;
    private final ReturnDeliveryUpdateLogRepository returnDeliveryUpdateLogRepository;

    @Override
    public Page<GetAllReturnDeliveryUpdateLogResponseDto> getAllReturnDeliveryUpdateLogs(int page, int size, String sort) {
        Page<GetAllReturnDeliveryUpdateLogResponseDto> responseDtos = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<ReturnDeliveryUpdateLog> returnDeliveryUpdateLogs = returnDeliveryUpdateLogRepository.findAll(pageable);

        responseDtos = returnDeliveryUpdateLogs.map(returnDeliveryUpdateLog -> GetAllReturnDeliveryUpdateLogResponseDto.builder()
            .id(returnDeliveryUpdateLog.getId())
            .returnDeliveryId(returnDeliveryUpdateLog.getReturnDelivery().getId())
            .username(returnDeliveryUpdateLog.getChangedByUsername())
            .type(returnDeliveryUpdateLog.getType())
            .prevData(returnDeliveryUpdateLog.getPrevData())
            .newData(returnDeliveryUpdateLog.getNewData())
            .createdAt(DateUtils.format(returnDeliveryUpdateLog.getCreatedAt()))
            .build());

        return responseDtos;
    }

    @Override
    public Page<GetAllReturnDeliveryStatusLogResponseDto> getAllReturnDeliveryStatusLogs(int page, int size, String sort) {
        Page<GetAllReturnDeliveryStatusLogResponseDto> responseDtos = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<ReturnDeliveryStatusLog> returnDeliveryStatusLogs = returnDeliveryStatusLogRepository.findAll(pageable);

        responseDtos = returnDeliveryStatusLogs.map(returnDeliveryStatusLog -> GetAllReturnDeliveryStatusLogResponseDto.builder()
            .id(returnDeliveryStatusLog.getId())
            .returnDeliveryId(returnDeliveryStatusLog.getReturnDelivery().getId())
            .username(returnDeliveryStatusLog.getChangedByUsername())
            .changeReason(returnDeliveryStatusLog.getChangeReason())
            .prevStatus(returnDeliveryStatusLog.getPrevStatus())
            .newStatus(returnDeliveryStatusLog.getNewStatus())
            .createdAt(DateUtils.format(returnDeliveryStatusLog.getCreatedAt()))
            .build());

        return responseDtos;
    }
}

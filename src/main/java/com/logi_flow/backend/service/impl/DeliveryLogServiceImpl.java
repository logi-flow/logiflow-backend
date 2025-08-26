package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.util.DateUtils;
import com.logi_flow.backend.common.util.SortUtils;
import com.logi_flow.backend.dto.deliveryLog.response.GetAllDeliveryStatusLogResponseDto;
import com.logi_flow.backend.dto.deliveryLog.response.GetAllDeliveryUpdateLogResponseDto;
import com.logi_flow.backend.dto.retunDeliveryLog.response.GetAllReturnDeliveryUpdateLogResponseDto;
import com.logi_flow.backend.entity.DeliveryStatusLog;
import com.logi_flow.backend.entity.DeliveryUpdateLog;
import com.logi_flow.backend.entity.ReturnDeliveryUpdateLog;
import com.logi_flow.backend.repository.DeliveryStatusLogRepository;
import com.logi_flow.backend.repository.DeliveryUpdateLogRepository;
import com.logi_flow.backend.service.DeliveryLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryLogServiceImpl implements DeliveryLogService {
    private final DeliveryUpdateLogRepository deliveryUpdateLogRepository;
    private final DeliveryStatusLogRepository deliveryStatusLogRepository;

    @Override
    public Page<GetAllDeliveryUpdateLogResponseDto> getAllDeliveryUpdateLogs(int page, int size, String sort) {
        Page<GetAllDeliveryUpdateLogResponseDto> responseDtos = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<DeliveryUpdateLog> deliveryUpdateLogs = deliveryUpdateLogRepository.findAll(pageable);

        responseDtos = deliveryUpdateLogs.map(deliveryUpdateLog -> GetAllDeliveryUpdateLogResponseDto.builder()
            .id(deliveryUpdateLog.getId())
            .deliveryId(deliveryUpdateLog.getDelivery().getId())
            .username(deliveryUpdateLog.getChangedByUsername())
            .type(deliveryUpdateLog.getType())
            .prevData(deliveryUpdateLog.getPrevData())
            .newData(deliveryUpdateLog.getNewData())
            .createdAt(DateUtils.format(deliveryUpdateLog.getCreatedAt()))
            .build());

        return responseDtos;
    }

    @Override
    public Page<GetAllDeliveryStatusLogResponseDto> getAllDeliveryStatusLogs(int page, int size, String sort) {
        Page<GetAllDeliveryStatusLogResponseDto> responseDtos = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<DeliveryStatusLog> deliveryStatusLogs = deliveryStatusLogRepository.findAll(pageable);

        responseDtos = deliveryStatusLogs.map(deliveryStatusLog -> GetAllDeliveryStatusLogResponseDto.builder()
            .id(deliveryStatusLog.getId())
            .deliveryId(deliveryStatusLog.getDelivery().getId())
            .username(deliveryStatusLog.getChangedByUsername())
            .changeReason(deliveryStatusLog.getChangeReason())
            .prevStatus(deliveryStatusLog.getPrevStatus())
            .newStatus(deliveryStatusLog.getNewStatus())
            .createdAt(DateUtils.format(deliveryStatusLog.getCreatedAt()))
            .build());

        return responseDtos;
    }
}

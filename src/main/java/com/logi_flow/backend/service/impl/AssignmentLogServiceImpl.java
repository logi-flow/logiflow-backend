package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.util.DateUtils;
import com.logi_flow.backend.common.util.SortUtils;
import com.logi_flow.backend.dto.assignment.response.AssignmentStatusLogResponseDto;
import com.logi_flow.backend.dto.assignment.response.AssignmentUpdateLogResponseDto;
import com.logi_flow.backend.entity.AssignmentStatusLog;
import com.logi_flow.backend.entity.AssignmentUpdateLog;
import com.logi_flow.backend.repository.AssignmentStatusLogRepository;
import com.logi_flow.backend.repository.AssignmentUpdateLogRepository;
import com.logi_flow.backend.service.AssignmentLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class AssignmentLogServiceImpl implements AssignmentLogService {

    private final AssignmentStatusLogRepository assignmentStatusLogRepository;
    private final AssignmentUpdateLogRepository assignmentUpdateLogRepository;

    @Override
    public Page<AssignmentStatusLogResponseDto> getStatusLog(int page, int size, String sort) {
        Page<AssignmentStatusLogResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<AssignmentStatusLog> assignmentStatusLogs = assignmentStatusLogRepository.findAll(pageable);

        data = assignmentStatusLogs.map(this::toAssignmentStatusLogResponseDto);

        return data;
    }

    @Override
    public Page<AssignmentUpdateLogResponseDto> getUpdateLog(int page, int size, String sort) {
        Page<AssignmentUpdateLogResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<AssignmentUpdateLog> assignmentUpdateLogs = assignmentUpdateLogRepository.findAll(pageable);

        data = assignmentUpdateLogs.map(this::toAssignmentUpdateLogResponseDto);

        return data;
    }

    private AssignmentStatusLogResponseDto toAssignmentStatusLogResponseDto(AssignmentStatusLog assignmentStatusLog) {
        return AssignmentStatusLogResponseDto.builder()
                .id(assignmentStatusLog.getId())
                .driverName(assignmentStatusLog.getAssignment().getDriver().getName())
                .vehicleNumber(assignmentStatusLog.getAssignment().getVehicle().getVehicleNumber())
                .changedByUsername(assignmentStatusLog.getChangedByUsername())
                .changeReason(assignmentStatusLog.getChangeReason())
                .prevStatus(assignmentStatusLog.getPrevStatus())
                .newStatus(assignmentStatusLog.getNewStatus())
                .createdAt(DateUtils.format(assignmentStatusLog.getCreatedAt()))
                .build();
    }

    private AssignmentUpdateLogResponseDto toAssignmentUpdateLogResponseDto(AssignmentUpdateLog assignmentUpdateLog) {
        return AssignmentUpdateLogResponseDto.builder()
                .id(assignmentUpdateLog.getId())
                .driverName(assignmentUpdateLog.getAssignment().getDriver().getName())
                .vehicleNumber(assignmentUpdateLog.getAssignment().getVehicle().getVehicleNumber())
                .changedByUsername(assignmentUpdateLog.getChangedByUsername())
                .type(assignmentUpdateLog.getType())
                .prevData(assignmentUpdateLog.getPrevData())
                .newData(assignmentUpdateLog.getNewData())
                .createdAt(DateUtils.format(assignmentUpdateLog.getCreatedAt()))
                .build();
    }
}

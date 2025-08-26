package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.util.DateUtils;
import com.logi_flow.backend.common.util.SortUtils;
import com.logi_flow.backend.dto.userLog.response.GetUserRoleLogResponseDto;
import com.logi_flow.backend.dto.userLog.response.GetUserStatusLogResponseDto;
import com.logi_flow.backend.entity.UserRoleLog;
import com.logi_flow.backend.entity.UserStatusLog;
import com.logi_flow.backend.repository.UserRoleLogRepository;
import com.logi_flow.backend.repository.UserStatusLogRepository;
import com.logi_flow.backend.service.UserLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserLogServiceImpl implements UserLogService {
    private final UserStatusLogRepository userStatusLogRepository;
    private final UserRoleLogRepository userRoleLogRepository;

    @Override
    public Page<GetUserStatusLogResponseDto> getUserStatusLogs(int page, int size, String sort) {
        Page<GetUserStatusLogResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<UserStatusLog> userStatusLogs = userStatusLogRepository.findAll(pageable);

        data = userStatusLogs.map(this::toGetUserStatusLogResponseDto);
        return data;
    }

    @Override
    public Page<GetUserRoleLogResponseDto> getUserRoleLogs(int page, int size, String sort) {
        Page<GetUserRoleLogResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<UserRoleLog> userRoleLogs = userRoleLogRepository.findAll(pageable);

        data = userRoleLogs.map(this::toGetUserRoleLogResponseDto);
        return data;
    }

    private GetUserStatusLogResponseDto toGetUserStatusLogResponseDto (UserStatusLog userStatusLog) {
        return GetUserStatusLogResponseDto.builder()
                .id(userStatusLog.getId())
                .userId(userStatusLog.getUser().getId())
                .username(userStatusLog.getChangedByUsername())
                .changeReason(userStatusLog.getChangeReason())
                .prevStatus(userStatusLog.getPrevStatus())
                .newStatus(userStatusLog.getNewStatus())
                .createdAt(DateUtils.format(userStatusLog.getCreatedAt()))
                .build();
    }

    private GetUserRoleLogResponseDto toGetUserRoleLogResponseDto (UserRoleLog userRoleLog) {
        return GetUserRoleLogResponseDto.builder()
                .id(userRoleLog.getId())
                .userId(userRoleLog.getUser().getId())
                .username(userRoleLog.getChangedByUsername())
                .changeReason(userRoleLog.getChangeReason())
                .prevRole(userRoleLog.getPrevRole())
                .newRole(userRoleLog.getNewRole())
                .createdAt(DateUtils.format(userRoleLog.getCreatedAt()))
                .build();
    }
}

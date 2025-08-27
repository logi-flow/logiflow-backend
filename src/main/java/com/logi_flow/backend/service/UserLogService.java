package com.logi_flow.backend.service;

import com.logi_flow.backend.dto.userLog.response.GetUserRoleLogResponseDto;
import com.logi_flow.backend.dto.userLog.response.GetUserStatusLogResponseDto;
import org.springframework.data.domain.Page;

@Service
public interface UserLogService {
    Page<GetUserStatusLogResponseDto> getUserStatusLogs(int page, int size, String sort);

    Page<GetUserRoleLogResponseDto> getUserRoleLogs(int page, int size, String sort);
}

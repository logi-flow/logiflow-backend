package com.logi_flow.backend.service;

import com.logi_flow.backend.dto.assignment.response.AssignmentStatusLogResponseDto;
import com.logi_flow.backend.dto.assignment.response.AssignmentUpdateLogResponseDto;
import org.springframework.data.domain.Page;

public interface AssignmentLogService {
    Page<AssignmentStatusLogResponseDto> getStatusLog(int page, int size, String sort);

    Page<AssignmentUpdateLogResponseDto> getUpdateLog(int page, int size, String sort);
}

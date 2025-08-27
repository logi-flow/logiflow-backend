package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.common.mapper.PageMapper;
import com.logi_flow.backend.dto.PageDto;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.assignment.response.AssignmentStatusLogResponseDto;
import com.logi_flow.backend.dto.assignment.response.AssignmentUpdateLogResponseDto;
import com.logi_flow.backend.service.AssignmentLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.ASSIGNMENT_API + "/logs")
public class AssignmentLogController {

    private final AssignmentLogService assignmentLogService;


    private static final String STATUS_API = "/status";
    private static final String UPDATE_API = "/update";

    @GetMapping(STATUS_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'ALLOCATIONS_MANAGER')")
    public ResponseEntity<ResponseDto<PageDto<AssignmentStatusLogResponseDto>>> getStatusLog(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<AssignmentStatusLogResponseDto> result = assignmentLogService.getStatusLog(page, size, sort);
        PageDto<AssignmentStatusLogResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping(UPDATE_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'ALLOCATIONS_MANAGER')")
    public ResponseEntity<ResponseDto<PageDto<AssignmentUpdateLogResponseDto>>> getUpdateLog(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<AssignmentUpdateLogResponseDto> result = assignmentLogService.getUpdateLog(page, size, sort);
        PageDto<AssignmentUpdateLogResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }
}

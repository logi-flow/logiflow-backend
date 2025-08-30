package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.common.mapper.PageMapper;
import com.logi_flow.backend.dto.PageDto;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.userLog.response.GetUserRoleLogResponseDto;
import com.logi_flow.backend.dto.userLog.response.GetUserStatusLogResponseDto;
import com.logi_flow.backend.service.UserLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사용자 로그 관리", description = "사용자(User) 관련 로그 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.USER_API)
public class UserLogController {
    private final UserLogService userLogService;

    private static final String STATUS_LOG = "/logs/status";
    private static final String ROLE_LOG = "/logs/roles";

    @Operation(summary = "사용자 상태 변경 조회", description = "로그 조회")
    @GetMapping(STATUS_LOG)
    public ResponseEntity<ResponseDto<PageDto<GetUserStatusLogResponseDto>>> getUserStatusLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetUserStatusLogResponseDto> result = userLogService.getUserStatusLogs(page, size, sort);
        PageDto<GetUserStatusLogResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "사용자 정보 변경 조회", description = "로그 조회")
    @GetMapping(ROLE_LOG)
    public ResponseEntity<ResponseDto<PageDto<GetUserRoleLogResponseDto>>> getUserRoleLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetUserRoleLogResponseDto> result = userLogService.getUserRoleLogs(page, size, sort);
        PageDto<GetUserRoleLogResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }
}

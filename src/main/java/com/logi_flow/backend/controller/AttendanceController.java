package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.common.mapper.PageMapper;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.PageDto;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.attendance.request.UpdateAttendanceRequestDto;
import com.logi_flow.backend.dto.attendance.response.*;
import com.logi_flow.backend.service.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.ATTENDANCE_API)
public class AttendanceController {
    private final AttendanceService attendanceService;

    private static final String CHECK_IN_API = "/check-in";
    private static final String CHECK_OUT_API = "/check-out";
    private static final String GET_DETAILS_API = "/{attendanceId}";
    private static final String MY_ATTENDANCE_API = "/me";
    private static final String MY_ATTENDANCE_LIST_API = "/me/list";

    @PostMapping(CHECK_IN_API)
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<ResponseDto<CreateAttendanceResponseDto>> checkInAttendance(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        ResponseDto<CreateAttendanceResponseDto> response = attendanceService.checkInAttendance(userPrincipal);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    @PutMapping(CHECK_OUT_API)
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<ResponseDto<UpdateAttendanceResponseDto>> checkOutAttendance(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody UpdateAttendanceRequestDto dto
    ) {
        ResponseDto<UpdateAttendanceResponseDto> response = attendanceService.checkOutAttendance(userPrincipal, dto);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HUMAN_RESOURCES_MANAGER')")
    public ResponseEntity<ResponseDto<PageDto<GetAllAttendanceResponseDto>>> getAllAttendance(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetAllAttendanceResponseDto> result = attendanceService.getAllAttendance(page, size, sort);
        PageDto<GetAllAttendanceResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping(GET_DETAILS_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'HUMAN_RESOURCES_MANAGER')")
    public ResponseEntity<ResponseDto<GetAttendanceDetailResponseDto>> getAttendanceDetail(
            @PathVariable Long attendanceId
    ) {
        ResponseDto<GetAttendanceDetailResponseDto> response = attendanceService.getAttendanceDetails(attendanceId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping(MY_ATTENDANCE_API)
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<ResponseDto<GetMyAttendanceDetailResponseDto>> getMyAttendance(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        ResponseDto<GetMyAttendanceDetailResponseDto> response = attendanceService.getMyAttendance(userPrincipal);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping(MY_ATTENDANCE_LIST_API)
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<ResponseDto<PageDto<GetAllMyAttendanceResponseDto>>> getAllMyAttendance(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetAllMyAttendanceResponseDto> result = attendanceService.getAllMyAttendance(userPrincipal, page, size, sort);
        PageDto<GetAllMyAttendanceResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }
}

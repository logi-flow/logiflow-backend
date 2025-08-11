package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.attendance.response.*;
import com.logi_flow.backend.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping(CHECK_IN_API)
    public ResponseEntity<ResponseDto<CreateAttendanceResponseDto>> checkInAttendance(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        ResponseDto<CreateAttendanceResponseDto> response = attendanceService.checkInAttendance(userPrincipal);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    @PutMapping(CHECK_OUT_API)
    public ResponseEntity<ResponseDto<UpdateAttendanceResponseDto>> checkOutAttendance(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        ResponseDto<UpdateAttendanceResponseDto> response = attendanceService.checkOutAttendance(userPrincipal);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<GetAllAttendanceResponseDto>> getAllAttendance() {
        ResponseDto<GetAllAttendanceResponseDto> response = attendanceService.getAllAttendance();
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping(GET_DETAILS_API)
    public ResponseEntity<ResponseDto<GetAllAttendanceResponseDto>> getAttendanceDetails(
            @PathVariable Long attendanceId
    ) {
        ResponseDto<GetAllAttendanceResponseDto> response = attendanceService.getAttendanceDetails(attendanceId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping(MY_ATTENDANCE_API)
    public ResponseEntity<ResponseDto<GetMyAttendancesResponseDto>> getMyAttendances(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        ResponseDto<GetMyAttendancesResponseDto> response = attendanceService.getMyAttendances(userPrincipal);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }
}

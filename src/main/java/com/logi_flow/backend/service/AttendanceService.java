package com.logi_flow.backend.service;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.attendance.request.UpdateAttendanceRequestDto;
import com.logi_flow.backend.dto.attendance.response.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

public interface AttendanceService {
    ResponseDto<GetMyAttendanceDetailResponseDto> checkInAttendance(UserPrincipal userPrincipal);
    ResponseDto<GetMyAttendanceDetailResponseDto> checkOutAttendance(UserPrincipal userPrincipal, @Valid UpdateAttendanceRequestDto dto);
    Page<GetAllAttendanceResponseDto> getAllAttendance(int page, int size, String sort);
    ResponseDto<GetAttendanceDetailResponseDto> getAttendanceDetails(Long attendanceId);
    ResponseDto<GetMyAttendanceDetailResponseDto> getMyAttendance(UserPrincipal userPrincipal);
    Page<GetAllMyAttendanceResponseDto> getAllMyAttendance(UserPrincipal userPrincipal, int page, int size, String sort);
}

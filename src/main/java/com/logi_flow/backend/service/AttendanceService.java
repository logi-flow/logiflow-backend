package com.logi_flow.backend.service;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.attendance.response.*;
import org.springframework.data.domain.Page;

public interface AttendanceService {
    ResponseDto<CreateAttendanceResponseDto> checkInAttendance(UserPrincipal userPrincipal);
    ResponseDto<UpdateAttendanceResponseDto> checkOutAttendance(UserPrincipal userPrincipal);
    Page<GetAllAttendanceResponseDto> getAllAttendance(int page, int size, String sort);
    ResponseDto<GetAttendanceDetailResponseDto> getAttendanceDetails(Long attendanceId);
    ResponseDto<GetMyAttendanceDetailResponseDto> getMyAttendance(UserPrincipal userPrincipal);
    Page<GetAllMyAttendanceResponseDto> getAllMyAttendance(UserPrincipal userPrincipal, int page, int size, String sort);
}

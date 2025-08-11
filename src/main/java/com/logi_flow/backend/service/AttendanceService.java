package com.logi_flow.backend.service;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.attendance.response.CreateAttendanceResponseDto;
import com.logi_flow.backend.dto.attendance.response.GetAllAttendanceResponseDto;
import com.logi_flow.backend.dto.attendance.response.GetMyAttendancesResponseDto;
import com.logi_flow.backend.dto.attendance.response.UpdateAttendanceResponseDto;

public interface AttendanceService {
    ResponseDto<CreateAttendanceResponseDto> checkInAttendance(UserPrincipal userPrincipal);
    ResponseDto<UpdateAttendanceResponseDto> checkOutAttendance(UserPrincipal userPrincipal);
    ResponseDto<GetAllAttendanceResponseDto> getAllAttendance();
    ResponseDto<GetAllAttendanceResponseDto> getAttendanceDetails(Long attendanceId);
    ResponseDto<GetMyAttendancesResponseDto> getMyAttendances(UserPrincipal userPrincipal);

}

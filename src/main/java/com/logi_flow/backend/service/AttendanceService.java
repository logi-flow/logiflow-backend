package com.logi_flow.backend.service;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.attendance.response.CreateAttendanceResponseDto;
import com.logi_flow.backend.dto.attendance.response.GetAllAttendanceResponseDto;
import com.logi_flow.backend.dto.attendance.response.GetMyAttendancesResponseDto;
import com.logi_flow.backend.dto.attendance.response.UpdateAttendanceResponseDto;
import org.springframework.data.domain.Page;

public interface AttendanceService {
    ResponseDto<CreateAttendanceResponseDto> checkInAttendance(UserPrincipal userPrincipal);
    ResponseDto<UpdateAttendanceResponseDto> checkOutAttendance(UserPrincipal userPrincipal);
    Page<GetAllAttendanceResponseDto> getAllAttendance(int page, int size, String sort);
    ResponseDto<GetAllAttendanceResponseDto> getAttendanceDetails(Long attendanceId);
    Page<GetMyAttendancesResponseDto> getMyAttendances(UserPrincipal userPrincipal, int page, int size, String sort);

}

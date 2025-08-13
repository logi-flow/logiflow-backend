package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.attendance.response.CreateAttendanceResponseDto;
import com.logi_flow.backend.dto.attendance.response.GetAllAttendanceResponseDto;
import com.logi_flow.backend.dto.attendance.response.GetMyAttendancesResponseDto;
import com.logi_flow.backend.dto.attendance.response.UpdateAttendanceResponseDto;
import com.logi_flow.backend.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AttendanceServiceImpl implements AttendanceService {
    @Override
    public ResponseDto<CreateAttendanceResponseDto> checkInAttendance(UserPrincipal userPrincipal) {
        return null;
    }

    @Override
    public ResponseDto<UpdateAttendanceResponseDto> checkOutAttendance(UserPrincipal userPrincipal) {
        return null;
    }

    @Override
    public Page<GetAllAttendanceResponseDto> getAllAttendance(int page, int size, String sort) {
        return null;
    }

    @Override
    public ResponseDto<GetAllAttendanceResponseDto> getAttendanceDetails(Long attendanceId) {
        return null;
    }

    @Override
    public Page<GetMyAttendancesResponseDto> getMyAttendances(UserPrincipal userPrincipal, int page, int size, String sort) {
        return null;
    }
}

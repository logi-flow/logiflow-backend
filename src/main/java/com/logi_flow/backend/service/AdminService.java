package com.logi_flow.backend.service;

import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.admin.request.AdminResetPasswordRequestDto;
import com.logi_flow.backend.dto.admin.response.AdminResetPasswordResponseDto;
import jakarta.validation.Valid;

public interface AdminService {
    ResponseDto<AdminResetPasswordResponseDto> adminPasswordResetCustomer(@Valid AdminResetPasswordRequestDto dto);
}

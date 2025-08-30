package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.admin.request.AdminResetPasswordRequestDto;
import com.logi_flow.backend.dto.admin.response.AdminResetPasswordResponseDto;
import com.logi_flow.backend.dto.auth.request.CustomerPasswordResetRequestDto;
import com.logi_flow.backend.dto.auth.response.CustomerPasswordResetResponseDto;
import com.logi_flow.backend.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.ADMIN_API)
public class AdminController {
    private final AdminService adminService;

    @Operation(summary = "고객사 비밀번호 초기화", description = "관리자의 고객사 비밀번호 초기화")
    @PostMapping()
    public ResponseEntity<ResponseDto<AdminResetPasswordResponseDto>> adminPasswordResetCustomer(
            @Valid @RequestBody AdminResetPasswordRequestDto dto
    ) {
        ResponseDto<AdminResetPasswordResponseDto> response = adminService.adminPasswordResetCustomer(dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }
}

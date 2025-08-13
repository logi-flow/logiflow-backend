package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.user.request.UpdateUserRoleRequestDto;
import com.logi_flow.backend.dto.user.request.UpdateUserStatusRequestDto;
import com.logi_flow.backend.dto.user.response.GetAllUserResponseDto;
import com.logi_flow.backend.dto.user.response.GetUserDetailResponseDto;
import com.logi_flow.backend.dto.user.response.UpdateUserRoleResponseDto;
import com.logi_flow.backend.dto.user.response.UpdateUserStatusResponseDto;
import com.logi_flow.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    @Override
    public ResponseDto<GetAllUserResponseDto> getAllUser(UserPrincipal userPrincipal) {
        return null;
    }

    @Override
    public ResponseDto<GetUserDetailResponseDto> getUserDetail(UserPrincipal userPrincipal, Long userId) {
        return null;
    }

    @Override
    public ResponseDto<UpdateUserStatusResponseDto> updateUserStatus(UserPrincipal userPrincipal, Long userId, UpdateUserStatusRequestDto dto) {
        return null;
    }

    @Override
    public ResponseDto<UpdateUserRoleResponseDto> updateUserRole(UserPrincipal userPrincipal, Long userId, UpdateUserRoleRequestDto dto) {
        return null;
    }
}

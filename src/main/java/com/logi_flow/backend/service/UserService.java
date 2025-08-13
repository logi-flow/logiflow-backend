package com.logi_flow.backend.service;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.user.request.UpdateUserRoleRequestDto;
import com.logi_flow.backend.dto.user.request.UpdateUserStatusRequestDto;
import com.logi_flow.backend.dto.user.response.GetAllUserResponseDto;
import com.logi_flow.backend.dto.user.response.GetUserDetailResponseDto;
import com.logi_flow.backend.dto.user.response.UpdateUserRoleResponseDto;
import com.logi_flow.backend.dto.user.response.UpdateUserStatusResponseDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

public interface UserService {
    Page<GetAllUserResponseDto> getAllUser(UserPrincipal userPrincipal, int page, int size, String sort);

    ResponseDto<GetUserDetailResponseDto> getUserDetail(UserPrincipal userPrincipal, Long userId);

    ResponseDto<UpdateUserStatusResponseDto> updateUserStatus(UserPrincipal userPrincipal, Long userId, @Valid UpdateUserStatusRequestDto dto);

    ResponseDto<UpdateUserRoleResponseDto> updateUserRole(UserPrincipal userPrincipal, Long userId, @Valid UpdateUserRoleRequestDto dto);
}

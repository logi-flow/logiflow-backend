package com.logi_flow.backend.service;

import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.user.request.UpdateUserRoleRequestDto;
import com.logi_flow.backend.dto.user.request.UpdateUserStatusRequestDto;
import com.logi_flow.backend.dto.user.response.GetAllUserResponseDto;
import com.logi_flow.backend.dto.user.response.GetUserDetailResponseDto;
import com.logi_flow.backend.dto.user.response.UpdateUserRoleResponseDto;
import com.logi_flow.backend.dto.user.response.UpdateUserStatusResponseDto;
import jakarta.validation.Valid;

public interface UserService {
    ResponseDto<GetAllUserResponseDto> getAllUser(Long id);

    ResponseDto<GetUserDetailResponseDto> getUserDetail(Long id, Long userId);

    ResponseDto<UpdateUserStatusResponseDto> updateUserStatus(Long id, Long userId, @Valid UpdateUserStatusRequestDto dto);

    ResponseDto<UpdateUserRoleResponseDto> updateUserRole(Long id, Long userId, @Valid UpdateUserRoleRequestDto dto);
}

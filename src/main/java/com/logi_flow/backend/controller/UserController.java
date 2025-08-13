package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.customer.request.UpdateCustomerStatusRequestDto;
import com.logi_flow.backend.dto.customer.response.UpdateCustomerStatusResponseDto;
import com.logi_flow.backend.dto.employee.response.GetAllEmployeeResponseDto;
import com.logi_flow.backend.dto.employee.response.GetEmployeeDetailResponseDto;
import com.logi_flow.backend.dto.user.request.UpdateUserRoleRequestDto;
import com.logi_flow.backend.dto.user.request.UpdateUserStatusRequestDto;
import com.logi_flow.backend.dto.user.response.GetAllUserResponseDto;
import com.logi_flow.backend.dto.user.response.GetUserDetailResponseDto;
import com.logi_flow.backend.dto.user.response.UpdateUserRoleResponseDto;
import com.logi_flow.backend.dto.user.response.UpdateUserStatusResponseDto;
import com.logi_flow.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.USER_API)
public class UserController {
    private final UserService userService;

    private static final String USER_ID_API = "/{userId}";
    private static final String USER_STATUS_API = "/{userId}/status";
    private static final String USER_ROLE_API = "/{userId}/roles";
    private static final String PROFILE_IMAGE_API = "/profile-image";

    @GetMapping
    public ResponseEntity<ResponseDto<GetAllUserResponseDto>> getAllUser(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        ResponseDto<GetAllUserResponseDto> response = userService.getAllUser(userPrincipal);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping(USER_ID_API)
    public ResponseEntity<ResponseDto<GetUserDetailResponseDto>> getUserDetail(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long userId
    ) {
        ResponseDto<GetUserDetailResponseDto> response = userService.getUserDetail(userPrincipal, userId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @PutMapping(USER_STATUS_API)
    public ResponseEntity<ResponseDto<UpdateUserStatusResponseDto>> updateUserStatus(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserStatusRequestDto dto
    ){
        ResponseDto<UpdateUserStatusResponseDto> response = userService.updateUserStatus(userPrincipal, userId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @PutMapping(USER_ROLE_API)
    public ResponseEntity<ResponseDto<UpdateUserRoleResponseDto>> updateUserRole(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserRoleRequestDto dto
    ){
        ResponseDto<UpdateUserRoleResponseDto> response = userService.updateUserRole(userPrincipal, userId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }
}

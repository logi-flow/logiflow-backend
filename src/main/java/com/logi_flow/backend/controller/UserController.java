package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.common.mapper.PageMapper;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.PageDto;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.user.request.UpdateUserRoleRequestDto;
import com.logi_flow.backend.dto.user.request.UpdateUserStatusRequestDto;
import com.logi_flow.backend.dto.user.response.GetAllUserResponseDto;
import com.logi_flow.backend.dto.user.response.GetUserDetailResponseDto;
import com.logi_flow.backend.dto.user.response.UpdateUserRoleResponseDto;
import com.logi_flow.backend.dto.user.response.UpdateUserStatusResponseDto;
import com.logi_flow.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "사용자 관리", description = "사용자(User) 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.USER_API)
public class UserController {
    private final UserService userService;

    private static final String USER_ID_API = "/{userId}";
    private static final String USER_STATUS_API = "/{userId}/status";
    private static final String USER_ROLE_API = "/{userId}/roles";


    @Operation(summary = "전체 사용자 목록 조회", description = "전체 사용자 목록 조회함")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto<PageDto<GetAllUserResponseDto>>> getAllUser(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetAllUserResponseDto> result = userService.getAllUser(userPrincipal, page, size, sort);
        PageDto<GetAllUserResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "사용자 세부 정보 조회", description = "사용자 세부 정보 조회")
    @GetMapping(USER_ID_API)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto<GetUserDetailResponseDto>> getUserDetail(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long userId
    ) {
        ResponseDto<GetUserDetailResponseDto> response = userService.getUserDetail(userPrincipal, userId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }


    @Operation(summary = "사용자 상태 수정", description = "사용자의 상태를 수정")
    @PutMapping(USER_STATUS_API)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto<UpdateUserStatusResponseDto>> updateUserStatus(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserStatusRequestDto dto
    ){
        ResponseDto<UpdateUserStatusResponseDto> response = userService.updateUserStatus(userPrincipal, userId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "사용자 역할 수정", description = "사용자의 역할을 수정")
    @PutMapping(USER_ROLE_API)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto<UpdateUserRoleResponseDto>> updateUserRole(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserRoleRequestDto dto
    ){
        ResponseDto<UpdateUserRoleResponseDto> response = userService.updateUserRole(userPrincipal, userId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "사용자 삭제", description = "사용자 상태를 삭제로 변경")
    @DeleteMapping(USER_ID_API)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto<?>> deleteUser(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long userId
    ) {
        ResponseDto<?> response = userService.deleteUser(userPrincipal, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}

package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.constants.ResponseCode;
import com.logi_flow.backend.common.constants.ResponseMessage;
import com.logi_flow.backend.common.enums.TableRef;
import com.logi_flow.backend.common.enums.user.UserRole;
import com.logi_flow.backend.common.enums.user.UserStatus;
import com.logi_flow.backend.common.util.DateUtils;
import com.logi_flow.backend.common.util.SortUtils;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.employee.response.GetAllEmployeeResponseDto;
import com.logi_flow.backend.dto.user.request.UpdateUserRoleRequestDto;
import com.logi_flow.backend.dto.user.request.UpdateUserStatusRequestDto;
import com.logi_flow.backend.dto.user.response.GetAllUserResponseDto;
import com.logi_flow.backend.dto.user.response.GetUserDetailResponseDto;
import com.logi_flow.backend.dto.user.response.UpdateUserRoleResponseDto;
import com.logi_flow.backend.dto.user.response.UpdateUserStatusResponseDto;
import com.logi_flow.backend.entity.*;
import com.logi_flow.backend.repository.RoleRepository;
import com.logi_flow.backend.repository.UserRepository;
import com.logi_flow.backend.repository.UserRoleLogRepository;
import com.logi_flow.backend.repository.UserStatusLogRepository;
import com.logi_flow.backend.service.DeleteLogService;
import com.logi_flow.backend.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserStatusLogRepository userStatusLogRepository;
    private final RoleRepository roleRepository;
    private final UserRoleLogRepository userRoleLogRepository;
    private final DeleteLogService deleteLogService;

    @Override
    public Page<GetAllUserResponseDto> getAllUser(UserPrincipal userPrincipal, int page, int size, String sort) {
        Page<GetAllUserResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<User> users = userRepository.findAll(pageable);

        data = users.map(this::toGetAllUserResponseDto);

        return data;
    }

    @Override
    public ResponseDto<GetUserDetailResponseDto> getUserDetail(UserPrincipal userPrincipal, Long userId) {
        GetUserDetailResponseDto data = null;

        String username = userPrincipal.getUsername();
        User loginUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.USER_NOT_FOUND));

        if(!loginUser.getRole().getName().equals(UserRole.ADMIN)) {
            return ResponseDto.fail("FORBIDDEN", ResponseMessage.NO_PERMISSION);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        data = GetUserDetailResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole().getName())
                .status(user.getStatus())
                .createdAt(DateUtils.format(user.getCreatedAt()))
                .updatedAt(DateUtils.format(user.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<UpdateUserStatusResponseDto> updateUserStatus(UserPrincipal userPrincipal, Long userId, UpdateUserStatusRequestDto dto) {
        UpdateUserStatusResponseDto data = null;

        String username = userPrincipal.getUsername();
        User loginUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.USER_NOT_FOUND));

        if(!loginUser.getRole().getName().equals(UserRole.ADMIN)) {
            return ResponseDto.fail("FORBIDDEN", ResponseMessage.NO_PERMISSION);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        UserStatus prevStatus = user.getStatus();

        if (dto.getStatus() != prevStatus) {
            user.setStatus(dto.getStatus());
        }

        User updatedUser = userRepository.save(user);

        UserStatusLog userStatusLog = UserStatusLog.builder()
                .user(user)
                .changedBy(loginUser)
                .changedByUsername(username)
                .changeReason(dto.getChangedReason())
                .prevStatus(prevStatus)
                .newStatus(updatedUser.getStatus())
                .build();

        userStatusLogRepository.save(userStatusLog);

        data = UpdateUserStatusResponseDto.builder()
                .id(updatedUser.getId())
                .status(updatedUser.getStatus())
                .changedBy(user.getId())
                .changedByUsername(username)
                .changedReason(userStatusLog.getChangeReason())
                .prevStatus(prevStatus)
                .newStatus(updatedUser.getStatus())
                .createdAt(DateUtils.format(updatedUser.getCreatedAt()))
                .updatedAt(DateUtils.format(updatedUser.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<UpdateUserRoleResponseDto> updateUserRole(UserPrincipal userPrincipal, Long userId, UpdateUserRoleRequestDto dto) {
        UpdateUserRoleResponseDto data = null;

        String username = userPrincipal.getUsername();
        User loginUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.USER_NOT_FOUND));

        if(!loginUser.getRole().getName().equals(UserRole.ADMIN)) {
            return ResponseDto.fail("FORBIDDEN", ResponseMessage.NO_PERMISSION);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        UserRole prevRole = user.getRole().getName();

        if (dto.getRole() != prevRole) {
            Role newRole = roleRepository.findByName(dto.getRole())
                            .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.FAILED));
            user.setRole(newRole);
        }

        User updatedUser = userRepository.save(user);

        UserRoleLog userRoleLog = UserRoleLog.builder()
                .user(user)
                .changedBy(loginUser)
                .changedByUsername(username)
                .changeReason(dto.getChangedReason())
                .prevRole(prevRole)
                .newRole(updatedUser.getRole().getName())
                .build();

        userRoleLogRepository.save(userRoleLog);

        data = UpdateUserRoleResponseDto.builder()
                .id(updatedUser.getId())
                .role(updatedUser.getRole().getName())
                .changedBy(user.getId())
                .changedByUsername(username)
                .changedReason(userRoleLog.getChangeReason())
                .prevRole(prevRole)
                .newRole(updatedUser.getRole().getName())
                .createdAt(DateUtils.format(updatedUser.getCreatedAt()))
                .updatedAt(DateUtils.format(updatedUser.getUpdatedAt()))
                .build();


        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<?> deleteUser(UserPrincipal userPrincipal, Long userId) {
        String username = userPrincipal.getUsername();
        User loginUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.USER_NOT_FOUND));

        if(!loginUser.getRole().getName().equals(UserRole.ADMIN)) {
            return ResponseDto.fail("FORBIDDEN", ResponseMessage.NO_PERMISSION);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        if (user.getStatus() == UserStatus.DELETED) {
            return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        }

        user.setStatus(UserStatus.DELETED);
        userRepository.save(user);

        deleteLogService.createLog(TableRef.USER, userId, loginUser);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    private GetAllUserResponseDto toGetAllUserResponseDto (User user) {
        return GetAllUserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole().getName())
                .status(user.getStatus())
                .createdAt(DateUtils.format(user.getCreatedAt()))
                .updatedAt(DateUtils.format(user.getUpdatedAt()))
                .build();
    }
}

package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.constants.ResponseCode;
import com.logi_flow.backend.common.constants.ResponseMessage;
import com.logi_flow.backend.common.enums.ContractStatus;
import com.logi_flow.backend.common.enums.CustomerStatus;
import com.logi_flow.backend.common.enums.TableRef;
import com.logi_flow.backend.common.enums.driver.DriverStatus;
import com.logi_flow.backend.common.enums.employee.EmployeeStatus;
import com.logi_flow.backend.common.enums.user.UserRole;
import com.logi_flow.backend.common.enums.user.UserStatus;
import com.logi_flow.backend.common.util.DateUtils;
import com.logi_flow.backend.common.util.SortUtils;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.user.request.UpdateUserRoleRequestDto;
import com.logi_flow.backend.dto.user.request.UpdateUserStatusRequestDto;
import com.logi_flow.backend.dto.user.response.GetAllUserResponseDto;
import com.logi_flow.backend.dto.user.response.GetUserDetailResponseDto;
import com.logi_flow.backend.dto.user.response.UpdateUserRoleResponseDto;
import com.logi_flow.backend.dto.user.response.UpdateUserStatusResponseDto;
import com.logi_flow.backend.entity.*;
import com.logi_flow.backend.repository.*;
import com.logi_flow.backend.service.AlertService;
import com.logi_flow.backend.service.DeleteLogService;
import com.logi_flow.backend.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final DriverRepository driverRepository;
    private final CustomerRepository customerRepository;
    private final ContractRepository contractRepository;
    private final UserStatusLogRepository userStatusLogRepository;
    private final RoleRepository roleRepository;
    private final UserRoleLogRepository userRoleLogRepository;
    private final DeleteLogService deleteLogService;
    private final AlertService alertService;

    @Override
    @Transactional(readOnly = true)
    public Page<GetAllUserResponseDto> getAllUser(UserPrincipal userPrincipal, int page, int size, String sort) {
        Page<GetAllUserResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<User> users = userRepository.findAll(pageable);

        data = users.map(this::toGetAllUserResponseDto);

        return data;
    }

    @Override
    @Transactional(readOnly = true)
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
    @Transactional
    public ResponseDto<UpdateUserStatusResponseDto> updateUserStatus(UserPrincipal userPrincipal, Long userId, UpdateUserStatusRequestDto dto) {
        UpdateUserStatusResponseDto data = null;

        String username = userPrincipal.getUsername();
        User loginUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.USER_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        if(!loginUser.getRole().getName().equals(UserRole.ADMIN)) {
            return ResponseDto.fail("FORBIDDEN", ResponseMessage.NO_PERMISSION);
        }

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

        String alertMessage = "[상태 변경] " + prevStatus + " → " + updatedUser.getStatus() +
                (dto.getChangedReason() != null ? (" (사유: " + dto.getChangedReason() + ")") : "");
        alertService.sendToUser(user.getId(), alertMessage);

        data = UpdateUserStatusResponseDto.builder()
                .id(updatedUser.getId())
                .status(updatedUser.getStatus())
                .changedBy(loginUser.getId())
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
    @Transactional
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

        String alertMessage = "[상태 변경] " + prevRole + " → " + updatedUser.getRole().getName() +
                (dto.getChangedReason() != null ? (" (사유: " + dto.getChangedReason() + ")") : "");
        alertService.sendToUser(user.getId(), alertMessage);

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
    @Transactional
    public ResponseDto<?> deleteUser(UserPrincipal userPrincipal, Long userId) {
        String username = userPrincipal.getUsername();
        User loginUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.USER_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Customer cus= customerRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        if (contractRepository.existsByCustomerAndStatus(cus, ContractStatus.APPROVED)) {
            return ResponseDto.success(ResponseCode.FAILED, "진행중인 계약이 있어 삭제가 불가능합니다.");
        }

        if (user.getStatus() == UserStatus.DELETED) {
            return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        }

        employeeRepository.findByUser(user).ifPresent(employee -> {
            if (employee.getStatus() != EmployeeStatus.RETIRED) {
                employee.setStatus(EmployeeStatus.RETIRED);
                employeeRepository.save(employee);
                deleteLogService.createLog(TableRef.EMPLOYEE, employee.getId(), loginUser);
                String alertMessage = "[퇴사 처리] 계정 상태가 RETIRED로 변경되었습니다.";
                alertService.sendToUser(employee.getUser().getId(), alertMessage);
            }
        });

        driverRepository.findByUser(user).ifPresent(driver -> {
            if (driver.getStatus() != DriverStatus.RETIRED) {
                driver.setStatus(DriverStatus.RETIRED);
                driverRepository.save(driver);
                deleteLogService.createLog(TableRef.DRIVER, driver.getId(), loginUser);
                String alertMessage = "[퇴사 처리] 계정 상태가 RETIRED로 변경되었습니다.";
                alertService.sendToUser(driver.getUser().getId(), alertMessage);
            }
        });

        customerRepository.findByUser(user).ifPresent(customer -> {
            if (customer.getStatus() != CustomerStatus.DELETED) {
                customer.setStatus(CustomerStatus.DELETED);
                customerRepository.save(customer);
                deleteLogService.createLog(TableRef.CUSTOMER, customer.getId(), loginUser);
            }
        });

        user.setStatus(UserStatus.DELETED);
        userRepository.save(user);

        deleteLogService.createLog(TableRef.USER, userId, loginUser);

        String alertMessage = "[계정 삭제] 계정 상태가 DELETED로 변경되었습니다.";
        alertService.sendToUser(user.getId(), alertMessage);

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

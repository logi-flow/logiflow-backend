package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.constants.ResponseCode;
import com.logi_flow.backend.common.constants.ResponseMessage;
import com.logi_flow.backend.common.enums.TableRef;
import com.logi_flow.backend.common.enums.employee.EmployeeStatus;
import com.logi_flow.backend.common.enums.user.UserRole;
import com.logi_flow.backend.common.enums.user.UserStatus;
import com.logi_flow.backend.common.util.DateUtils;
import com.logi_flow.backend.common.util.SortUtils;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.customer.response.GetAllCustomerResponseDto;
import com.logi_flow.backend.dto.employee.request.CreateEmployeeRequestDto;
import com.logi_flow.backend.dto.employee.request.UpdateEmployeeAdminRequestDto;
import com.logi_flow.backend.dto.employee.request.UpdateEmployeeRequestDto;
import com.logi_flow.backend.dto.employee.request.UpdateEmployeeStatusRequestDto;
import com.logi_flow.backend.dto.employee.response.*;
import com.logi_flow.backend.entity.*;
import com.logi_flow.backend.repository.*;
import com.logi_flow.backend.service.DeleteLogService;
import com.logi_flow.backend.service.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmployeeUpdateLogRepository employeeUpdateLogRepository;
    private final EmployeeStatusLogRepository employeeStatusLogRepository;
    private final DeleteLogService deleteLogService;

    @Override
    public ResponseDto<CreateEmployeeResponseDto> createEmployee(UserPrincipal userPrincipal, CreateEmployeeRequestDto dto) {
        CreateEmployeeResponseDto data = null;

        if (employeeRepository.findByPhoneNumber(dto.getPhoneNumber()).isPresent()) {
            throw new IllegalArgumentException(ResponseMessage.ALREADY_EXISTS);
        }

        String username = generateUsername(dto.getName(), dto.getPhoneNumber());

        String phoneNumber = dto.getPhoneNumber();
        if (phoneNumber == null || phoneNumber.length() < 4)
            throw  new IllegalArgumentException(ResponseMessage.FAILED);
        String password = phoneNumber.substring(phoneNumber.length() - 4);
        String encodePassword = bCryptPasswordEncoder.encode(password);

        Role role = roleRepository.findByName(UserRole.EMPLOYEE)
                .orElseGet(() -> roleRepository.save(Role.builder().name(UserRole.EMPLOYEE).build()));

        User user = User.builder()
                .role(role)
                .username(username)
                .password(encodePassword)
                .email(dto.getEmail())
                .status(UserStatus.ACTIVE)
                .build();

        userRepository.save(user);

        Employee employee = Employee.builder()
                .user(user)
                .name(dto.getName())
                .identityNumber(dto.getIdentityNumber())
                .phoneNumber(dto.getPhoneNumber())
                .zipcode(dto.getZipcode())
                .address(dto.getAddress())
                .addressDetail(dto.getAddressDetail())
                .department(dto.getDepartment())
                .position(dto.getPosition())
                .companyJoin(dto.getCompanyJoin())
                .build();

        employeeRepository.save(employee);

        String identityNumberMasked = dto.getIdentityNumber().substring(0,6) + "*******";

        data = CreateEmployeeResponseDto.builder()
                .id(employee.getId())
                .userId(employee.getUser().getId())
                .username(employee.getUser().getUsername())
                .name(employee.getName())
                .status(employee.getStatus())
                .identityNumberMasked(identityNumberMasked)
                .phoneNumber(employee.getPhoneNumber())
                .email(employee.getUser().getEmail())
                .zipcode(employee.getZipcode())
                .address(employee.getAddress())
                .addressDetail(employee.getAddressDetail())
                .department(employee.getDepartment())
                .position(employee.getPosition())
                .companyJoin(employee.getCompanyJoin())
                .createdAt(DateUtils.format(employee.getCreatedAt()))
                .updatedAt(DateUtils.format(employee.getUpdatedAt()))
                .build();
        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<UpdateEmployeeResponseDto> updateEmployee(UserPrincipal userPrincipal, UpdateEmployeeRequestDto dto) {
        UpdateEmployeeResponseDto data = null;

        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Employee employee = employeeRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        List<EmployeeUpdateLog> logs = new ArrayList<>();

        if (!Objects.equals(dto.getPhoneNumber(), employee.getPhoneNumber())) {
            logs.add(buildUpdateLog(employee, user, username, "phone_number", employee.getPhoneNumber(), dto.getPhoneNumber()));
            employee.setPhoneNumber(dto.getPhoneNumber());
        }

        if (!Objects.equals(dto.getZipcode(), employee.getZipcode())) {
            logs.add(buildUpdateLog(employee, user, username, "zipcode", employee.getZipcode(), dto.getZipcode()));
            employee.setZipcode(dto.getZipcode());
        }

        if (!Objects.equals(dto.getAddress(), employee.getAddress())) {
            logs.add(buildUpdateLog(employee, user, username, "address", employee.getAddress(), dto.getAddress()));
            employee.setAddress(dto.getAddress());
        }

        if (!Objects.equals(dto.getAddressDetail(), employee.getAddressDetail())) {
            logs.add(buildUpdateLog(employee, user, username, "address_detail", employee.getAddressDetail(), dto.getAddressDetail()));
            employee.setAddressDetail(dto.getAddressDetail());
        }

        employeeRepository.save(employee);
        employeeUpdateLogRepository.saveAll(logs);

        String identityNumberMasked = employee.getIdentityNumber().substring(0,6) + "*******";

        data = UpdateEmployeeResponseDto.builder()
                .id(employee.getId())
                .userId(employee.getUser().getId())
                .name(employee.getName())
                .status(employee.getStatus())
                .identityNumberMasked(identityNumberMasked)
                .phoneNumber(employee.getPhoneNumber())
                .email(employee.getUser().getEmail())
                .zipcode(employee.getZipcode())
                .address(employee.getAddress())
                .addressDetail(employee.getAddressDetail())
                .department(employee.getDepartment())
                .position(employee.getPosition())
                .companyJoin(employee.getCompanyJoin())
                .createdAt(DateUtils.format(employee.getCreatedAt()))
                .updatedAt(DateUtils.format(employee.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<GetEmployeeDetailResponseDto> getEmployeeDetail(UserPrincipal userPrincipal) {
        GetEmployeeDetailResponseDto data = null;

        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Employee employee = employeeRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        String identityNumberMasked = employee.getIdentityNumber().substring(0,6) + "*******";

        data = GetEmployeeDetailResponseDto.builder()
                .id(employee.getId())
                .userId(employee.getUser().getId())
                .name(employee.getName())
                .status(employee.getStatus())
                .identityNumberMasked(identityNumberMasked)
                .phoneNumber(employee.getPhoneNumber())
                .email(employee.getUser().getEmail())
                .zipcode(employee.getZipcode())
                .address(employee.getAddress())
                .addressDetail(employee.getAddressDetail())
                .department(employee.getDepartment())
                .position(employee.getPosition())
                .companyJoin(employee.getCompanyJoin())
                .createdAt(DateUtils.format(employee.getCreatedAt()))
                .updatedAt(DateUtils.format(employee.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<UpdateEmployeeResponseDto> updateEmployeeAdmin(UserPrincipal userPrincipal, Long employeeId, UpdateEmployeeAdminRequestDto dto) {
        UpdateEmployeeResponseDto data = null;

        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        if(!user.getRole().getName().equals(UserRole.ADMIN)) {
            return ResponseDto.fail("FORBIDDEN", ResponseMessage.NO_PERMISSION);
        }

        List<EmployeeUpdateLog> logs = new ArrayList<>();

        if (!Objects.equals(dto.getName(), employee.getName())) {
            logs.add(buildUpdateLog(employee, user, username, "name", employee.getName(), dto.getName()));
            employee.setName(dto.getName());
        }

        if (!Objects.equals(dto.getIdentityNumber(), employee.getIdentityNumber())) {
            logs.add(buildUpdateLog(employee, user, username, "identity_number", employee.getIdentityNumber(), dto.getIdentityNumber()));
            employee.setIdentityNumber(dto.getIdentityNumber());
        }

        if (!Objects.equals(dto.getPhoneNumber(), employee.getPhoneNumber())) {
            logs.add(buildUpdateLog(employee, user, username, "phone_number", employee.getPhoneNumber(), dto.getPhoneNumber()));
            employee.setPhoneNumber(dto.getPhoneNumber());
        }

        if (!Objects.equals(dto.getEmail(), employee.getUser().getEmail())) {
            logs.add(buildUpdateLog(employee, user, username, "email", employee.getUser().getEmail(), dto.getEmail()));
            employee.getUser().setEmail(dto.getEmail());
        }

        if (!Objects.equals(dto.getZipcode(), employee.getZipcode())) {
            logs.add(buildUpdateLog(employee, user, username, "zipcode", employee.getZipcode(), dto.getZipcode()));
            employee.setZipcode(dto.getZipcode());
        }

        if (!Objects.equals(dto.getAddress(), employee.getAddress())) {
            logs.add(buildUpdateLog(employee, user, username, "address", employee.getAddress(), dto.getAddress()));
            employee.setAddress(dto.getAddress());
        }

        if (!Objects.equals(dto.getAddressDetail(), employee.getAddressDetail())) {
            logs.add(buildUpdateLog(employee, user, username, "address_detail", employee.getAddressDetail(), dto.getAddressDetail()));
            employee.setAddressDetail(dto.getAddressDetail());
        }

        if (!Objects.equals(dto.getDepartment(), employee.getDepartment())) {
            logs.add(buildUpdateLog(employee, user, username, "department", employee.getDepartment().name(), dto.getDepartment().name()));
            employee.setDepartment(dto.getDepartment());
        }

        if (!Objects.equals(dto.getAddressDetail(), employee.getAddressDetail())) {
            logs.add(buildUpdateLog(employee, user, username, "position", employee.getAddressDetail(), dto.getAddressDetail()));
            employee.setAddressDetail(dto.getAddressDetail());
        }

        if (!Objects.equals(dto.getAddressDetail(), employee.getAddressDetail())) {
            logs.add(buildUpdateLog(employee, user, username, "company_join", employee.getAddressDetail(), dto.getAddressDetail()));
            employee.setAddressDetail(dto.getAddressDetail());
        }

        employeeRepository.save(employee);
        employeeUpdateLogRepository.saveAll(logs);

        String identityNumberMasked = employee.getIdentityNumber().substring(0,6) + "*******";

        data = UpdateEmployeeResponseDto.builder()
                .id(employee.getId())
                .userId(employee.getUser().getId())
                .name(employee.getName())
                .status(employee.getStatus())
                .identityNumberMasked(identityNumberMasked)
                .phoneNumber(employee.getPhoneNumber())
                .email(employee.getUser().getEmail())
                .zipcode(employee.getZipcode())
                .address(employee.getAddress())
                .addressDetail(employee.getAddressDetail())
                .department(employee.getDepartment())
                .position(employee.getPosition())
                .companyJoin(employee.getCompanyJoin())
                .createdAt(DateUtils.format(employee.getCreatedAt()))
                .updatedAt(DateUtils.format(employee.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<UpdateEmployeeStatusResponseDto> updateEmployeeStatus(UserPrincipal userPrincipal, Long employeeId, UpdateEmployeeStatusRequestDto dto) {
        UpdateEmployeeStatusResponseDto data = null;

        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        if(!user.getRole().getName().equals(UserRole.ADMIN)) {
            return ResponseDto.fail("FORBIDDEN", ResponseMessage.NO_PERMISSION);
        }

        EmployeeStatus prevStatus = employee.getStatus();

        if (dto.getStatus() != prevStatus) {
            employee.setStatus(dto.getStatus());
        }

        Employee updatedEmployee = employeeRepository.save(employee);

        EmployeeStatusLog employeeStatusLog = EmployeeStatusLog.builder()
                .employee(employee)
                .user(user)
                .changedByUsername(username)
                .changeReason(dto.getChangedReason())
                .prevStatus(prevStatus)
                .newStatus(updatedEmployee.getStatus())
                .build();

        employeeStatusLogRepository.save(employeeStatusLog);

        data = UpdateEmployeeStatusResponseDto.builder()
                .id(updatedEmployee.getId())
                .status(updatedEmployee.getStatus())
                .changedBy(user.getId())
                .changedByUsername(username)
                .changedReason(employeeStatusLog.getChangeReason())
                .prevStatus(prevStatus)
                .newStatus(updatedEmployee.getStatus())
                .createdAt(DateUtils.format(updatedEmployee.getCreatedAt()))
                .updatedAt(DateUtils.format(updatedEmployee.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public Page<GetAllEmployeeResponseDto> getAllEmployee(UserPrincipal userPrincipal, int page, int size, String sort) {
        Page<GetAllEmployeeResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<Employee> employees = employeeRepository.findAll(pageable);

        data = employees.map(this::toGetAllEmployeeResponseDto);

        return data;
    }

    @Override
    public ResponseDto<GetEmployeeDetailAdminResponseDto> getEmployeeDetailAdmin(UserPrincipal userPrincipal, Long employeeId) {
        GetEmployeeDetailAdminResponseDto data = null;

        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.USER_NOT_FOUND));

        if(!user.getRole().getName().equals(UserRole.ADMIN)) {
            return ResponseDto.fail("FORBIDDEN", ResponseMessage.NO_PERMISSION);
        }

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        data = GetEmployeeDetailAdminResponseDto.builder()
                .id(employee.getId())
                .userId(employee.getUser().getId())
                .name(employee.getName())
                .status(employee.getStatus())
                .identityNumber(employee.getIdentityNumber())
                .phoneNumber(employee.getPhoneNumber())
                .email(employee.getUser().getEmail())
                .zipcode(employee.getZipcode())
                .address(employee.getAddress())
                .addressDetail(employee.getAddressDetail())
                .department(employee.getDepartment())
                .position(employee.getPosition())
                .companyJoin(employee.getCompanyJoin())
                .createdAt(DateUtils.format(employee.getCreatedAt()))
                .updatedAt(DateUtils.format(employee.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<?> deleteEmployee(UserPrincipal userPrincipal, Long employeeId) {
        User user = userRepository.findByUsername(userPrincipal.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        if (employee.getStatus() == EmployeeStatus.RETIRED) {
            return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        }

        employee.setStatus(EmployeeStatus.RETIRED);
        employeeRepository.save(employee);

        deleteLogService.createLog(TableRef.EMPLOYEE, employeeId, user);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    private String toInitials(String name) {
        Map<Character, String> choseongMap = Map.ofEntries(
                Map.entry('ㄱ', "k"), Map.entry('ㄴ', "n"),
                Map.entry('ㄷ', "d"), Map.entry('ㄹ', "l"),
                Map.entry('ㅁ', "m"), Map.entry('ㅂ', "p"),
                Map.entry('ㅅ', "s"), Map.entry('ㅇ', "y"),
                Map.entry('ㅈ', "j"), Map.entry('ㅊ', "c"),
                Map.entry('ㅋ', "k"), Map.entry('ㅌ', "t"),
                Map.entry('ㅍ', "p"), Map.entry('ㅎ', "h"),
                Map.entry('ㄲ', "kk"), Map.entry('ㄸ', "dd"),
                Map.entry('ㅆ', "ss"), Map.entry('ㅉ', "jj")
                );

        char[] choseongList = {
                'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ',
                'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
        };

        StringBuilder sb = new StringBuilder();
        for (char c : name.toCharArray()) {
            if (c >= '가' && c <= '힣') {
                int uniVal = c - 0xAC00;
                int choseongIndex = uniVal / (21 * 28);
                char choseong = choseongList[choseongIndex];
                sb.append(choseongMap.getOrDefault(choseong, ""));
            }
        }
        return sb.length() > 0 ? sb.toString() : "user";
    }

    private String generateUsername(String name, String phoneNumber) {
        String initials = toInitials(name);
        String last4Digits = phoneNumber.substring(phoneNumber.length() - 4);

        String baseUsername = initials + last4Digits;
        String finalUsername = baseUsername;

        while (userRepository.findByUsername(finalUsername).isPresent()) {
            String randomSuffix = String.format("%02d", new Random().nextInt(100));
            finalUsername = baseUsername + randomSuffix;
        }
        return finalUsername;
    }

    private EmployeeUpdateLog buildUpdateLog(Employee employee, User user, String username, String type, String prevData, String newData) {
        return EmployeeUpdateLog.builder()
                .employee(employee)
                .user(user)
                .changedByUsername(username)
                .type(type)
                .prevData(String.valueOf(prevData))
                .newData(String.valueOf(newData))
                .build();
    }

    private GetAllEmployeeResponseDto toGetAllEmployeeResponseDto (Employee employee) {
        return GetAllEmployeeResponseDto.builder()
                .id(employee.getId())
                .userId(employee.getUser().getId())
                .name(employee.getName())
                .status(employee.getStatus())
                .department(employee.getDepartment())
                .position(employee.getPosition())
                .companyJoin(employee.getCompanyJoin())
                .createdAt(DateUtils.format(employee.getCreatedAt()))
                .updatedAt(DateUtils.format(employee.getUpdatedAt()))
                .build();
    }
}

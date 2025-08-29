package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.constants.ResponseCode;
import com.logi_flow.backend.common.constants.ResponseMessage;
import com.logi_flow.backend.common.enums.TableRef;
import com.logi_flow.backend.common.enums.driver.DriverStatus;
import com.logi_flow.backend.common.enums.user.UserRole;
import com.logi_flow.backend.common.enums.user.UserStatus;
import com.logi_flow.backend.common.util.DateUtils;
import com.logi_flow.backend.common.util.SortUtils;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.driver.request.*;
import com.logi_flow.backend.dto.driver.response.*;
import com.logi_flow.backend.entity.*;
import com.logi_flow.backend.repository.*;
import com.logi_flow.backend.service.AlertService;
import com.logi_flow.backend.service.DeleteLogService;
import com.logi_flow.backend.service.DriverService;
import com.logi_flow.backend.service.UploadFileService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DriverStatusLogRepository driverStatusLogRepository;
    private final DriverUpdateLogRepository driverUpdateLogRepository;
    private final DeleteLogService deleteLogService;
    private final AlertService alertService;
    private final UploadFileService uploadFileService;

    @Override
    public ResponseDto<CreateDriverResponseDto> createDriver(CreateDriverRequestDto dto, MultipartFile profileImage) {
        CreateDriverResponseDto data = null;

        if(driverRepository.findByPhoneNumber(dto.getPhoneNumber()).isPresent())
            throw new IllegalArgumentException(ResponseMessage.ALREADY_EXISTS);

        long driverCount = driverRepository.count();
        String username = String.format("driver%03d", driverCount + 1);

        Role role = roleRepository.findByName(UserRole.DRIVER)
                .orElseGet(() -> {
                    Role newRole = Role.builder()
                            .name(UserRole.DRIVER)
                            .build();
                    return roleRepository.save(newRole);
                });

        String phoneNumber = dto.getPhoneNumber();
        if (phoneNumber == null || phoneNumber.length() < 4)
            throw  new IllegalArgumentException(ResponseMessage.FAILED);
        String password = phoneNumber.substring(phoneNumber.length() - 4);
        String encodedPassword = passwordEncoder.encode(password);

        User newUser = User.builder()
                .role(role)
                .username(username)
                .password(encodedPassword)
                .email(dto.getEmail())
                .status(UserStatus.ACTIVE)
                .mustChangePassword(true)
                .build();

        userRepository.save(newUser);

        if (profileImage != null && !profileImage.isEmpty()) {
            newUser.setProfileImage(uploadFileService.uploadProfile(profileImage, newUser.getId()));
        }

        Driver newDriver = Driver.builder()
                .user(newUser)
                .status(dto.getStatus())
                .name(dto.getName())
                .identityNumber(dto.getIdentityNumber())
                .phoneNumber(dto.getPhoneNumber())
                .zipcode(dto.getZipcode())
                .address(dto.getAddress())
                .addressDetail(dto.getAddressDetail())
                .district(dto.getDistrict())
                .pay(dto.getPay())
                .companyJoin(dto.getCompanyJoin())
                .build();

        driverRepository.save(newDriver);

        data = CreateDriverResponseDto.builder()
                .driverId(newDriver.getId())
                .name(newDriver.getName())
                .username(newDriver.getUser().getUsername())
                .password(newDriver.getUser().getPassword())
                .createdAt(DateUtils.format(newDriver.getCreatedAt()))
                .updatedAt(DateUtils.format(newDriver.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<UpdateDriverResponseDto> updateDriver(UserPrincipal userPrincipal, UpdateDriverRequestDto dto) {
        UpdateDriverResponseDto data = null;

        User user = userRepository.findByUsername(userPrincipal.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Driver driver = driverRepository.findByUserId(userPrincipal.getId())
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        List<DriverUpdateLog> logs = new ArrayList<>();

        if (dto.getName() != null && !driver.getName().equals(dto.getName())) {
            logs.add(buildUpdateLog(driver, user, "name", driver.getName(), dto.getName()));
            driver.setName(dto.getName());
        }
        if (dto.getPhoneNumber() != null && !driver.getPhoneNumber().equals(dto.getPhoneNumber())) {
            logs.add(buildUpdateLog(driver, user, "phone_number", driver.getPhoneNumber(), dto.getPhoneNumber()));
            driver.setPhoneNumber(dto.getPhoneNumber());
        }
        if (dto.getZipcode() != null && !driver.getZipcode().equals(dto.getZipcode())) {
            logs.add(buildUpdateLog(driver, user, "zipcode", driver.getZipcode(), dto.getZipcode()));
            driver.setZipcode(dto.getZipcode());
        }
        if (dto.getAddress() != null && !driver.getAddress().equals(dto.getAddress())) {
            logs.add(buildUpdateLog(driver, user, "address", driver.getAddress(), dto.getAddress()));
            driver.setAddress(dto.getAddress());
        }
        if (dto.getAddressDetail() != null && !driver.getAddressDetail().equals(dto.getAddressDetail())) {
            logs.add(buildUpdateLog(driver, user, "address_detail", driver.getAddressDetail(), dto.getAddressDetail()));
            driver.setAddressDetail(dto.getAddressDetail());
        }

        driverRepository.save(driver);
        driverUpdateLogRepository.saveAll(logs);

        data = UpdateDriverResponseDto.builder()
                .driverId(driver.getId())
                .name(driver.getName())
                .createdAt(DateUtils.format(driver.getCreatedAt()))
                .updatedAt(DateUtils.format(driver.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<UpdateDriverResponseDto> updateDriverStatus(UserPrincipal userPrincipal, Long driverId, UpdateDriverStatusRequestDto dto) {
        UpdateDriverResponseDto data = null;

        User user = userRepository.findByUsername(userPrincipal.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        DriverStatus prevStatus = dto.getStatus();

        if (prevStatus != null && !driver.getStatus().equals(prevStatus)) {
            driver.setStatus(prevStatus);
        }

        DriverStatus newStatus = driver.getStatus();

        driverRepository.save(driver);

        DriverStatusLog driverStatusLog = DriverStatusLog.builder()
                .driver(driver)
                .user(user)
                .changedByUsername(user.getUsername())
                .changeReason(dto.getChangeReason())
                .prevStatus(prevStatus)
                .newStatus(newStatus)
                .build();

        driverStatusLogRepository.save(driverStatusLog);

        data = UpdateDriverResponseDto.builder()
                .driverId(driver.getId())
                .name(driver.getName())
                .createdAt(DateUtils.format(driver.getCreatedAt()))
                .updatedAt(DateUtils.format(driver.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<UpdateDriverPayResponseDto> updateDriverPay(UserPrincipal userPrincipal, Long driverId, UpdateDriverPayRequestDto dto) {
        UpdateDriverPayResponseDto data = null;

        User user = userRepository.findByUsername(userPrincipal.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        DriverUpdateLog log = null;

        if (dto.getPay() != null && !driver.getPay().equals(dto.getPay())) {
            log = buildUpdateLog(driver, user, "pay", String.valueOf(driver.getPay()), String.valueOf(dto.getPay()));
            driver.setPay(dto.getPay());
        }

        driverRepository.save(driver);
        if (log != null) {
            driverUpdateLogRepository.save(log);
        }

        data = UpdateDriverPayResponseDto.builder()
                .driverId(driver.getId())
                .name(driver.getName())
                .pay(driver.getPay())
                .createdAt(DateUtils.format(driver.getCreatedAt()))
                .updatedAt(DateUtils.format(driver.getUpdatedAt()))
                .build();

        String alertMessage = "기사 #" + driverId + "님의 월급이" + driver.getPay() + "로 변경되었습니다!";
        alertService.sendToUser(driver.getUser().getId(), alertMessage);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<UpdateDriverResponseDto> updateDriverByAdmin(UserPrincipal userPrincipal, Long driverId, UpdateDriverByAdminRequestDto dto) {
        UpdateDriverResponseDto data = null;

        User user = userRepository.findByUsername(userPrincipal.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        List<DriverUpdateLog> logs = new ArrayList<>();

        if (dto.getName() != null && !driver.getName().equals(dto.getName())) {
            logs.add(buildUpdateLog(driver, user, "name", driver.getName(), dto.getName()));
            driver.setName(dto.getName());
        }
        if (dto.getPhoneNumber() != null && !driver.getPhoneNumber().equals(dto.getPhoneNumber())) {
            logs.add(buildUpdateLog(driver, user, "phone_number", driver.getPhoneNumber(), dto.getPhoneNumber()));
            driver.setPhoneNumber(dto.getPhoneNumber());
        }
        if (dto.getZipcode() != null && !driver.getZipcode().equals(dto.getZipcode())) {
            logs.add(buildUpdateLog(driver, user, "zipcode", driver.getZipcode(), dto.getZipcode()));
            driver.setZipcode(dto.getZipcode());
        }
        if (dto.getAddress() != null && !driver.getAddress().equals(dto.getAddress())) {
            logs.add(buildUpdateLog(driver, user, "address", driver.getAddress(), dto.getAddress()));
            driver.setAddress(dto.getAddress());
        }
        if (dto.getAddressDetail() != null && !driver.getAddressDetail().equals(dto.getAddressDetail())) {
            logs.add(buildUpdateLog(driver, user, "address_detail", driver.getAddressDetail(), dto.getAddressDetail()));
            driver.setAddressDetail(dto.getAddressDetail());
        }
        if (dto.getDistrict() != null && !driver.getDistrict().equals(dto.getDistrict())) {
            logs.add(buildUpdateLog(driver, user, "district", String.valueOf(driver.getDistrict()), String.valueOf(dto.getDistrict())));
            driver.setDistrict(dto.getDistrict());
        }

        driverRepository.save(driver);
        driverUpdateLogRepository.saveAll(logs);

        data = UpdateDriverResponseDto.builder()
                .driverId(driver.getId())
                .name(driver.getName())
                .createdAt(DateUtils.format(driver.getCreatedAt()))
                .updatedAt(DateUtils.format(driver.getUpdatedAt()))
                .build();

        String alertMessage = "기사 #" + driverId + "님의 정보가 변경되었습니다. 확인해주세요!";
        alertService.sendToUser(driver.getUser().getId(), alertMessage);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GetAllDriverResponseDto> getAllDriver(int page, int size, String sort) {
        Page<GetAllDriverResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<Driver> drivers = driverRepository.findAll(pageable);

        data = drivers.map(this::toGetAllDriverResponseDto);

        return data;
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseDto<GetDriverDetailResponseDto> getDriverDetail(Long driverId) {
        GetDriverDetailResponseDto data = null;

        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        DriverLicense license = driver.getDriverLicense();

        data = GetDriverDetailResponseDto.builder()
                .driverId(driver.getId())
                .name(driver.getName())
                .username(driver.getUser().getUsername())
                .status(driver.getStatus())
                .PhoneNumber(driver.getPhoneNumber())
                .identityNumber(driver.getIdentityNumber())
                .zipcode(driver.getZipcode())
                .address(driver.getAddress())
                .addressDetail(driver.getAddressDetail())
                .district(driver.getDistrict())
                .pay(driver.getPay())
                .companyJoin(driver.getCompanyJoin())
                .driverType(license != null ? license.getType() : null)
                .createdAt(DateUtils.format(driver.getCreatedAt()))
                .updatedAt(DateUtils.format(driver.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseDto<GetDriverDetailResponseDto> getMyInfo(UserPrincipal userPrincipal) {
        GetDriverDetailResponseDto data = null;

        User user = userRepository.findByUsername(userPrincipal.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Driver driver = driverRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        DriverLicense license = driver.getDriverLicense();

        data = GetDriverDetailResponseDto.builder()
                .driverId(driver.getId())
                .name(driver.getName())
                .username(driver.getUser().getUsername())
                .status(driver.getStatus())
                .PhoneNumber(driver.getPhoneNumber())
                .identityNumber(driver.getIdentityNumber())
                .zipcode(driver.getZipcode())
                .address(driver.getAddress())
                .addressDetail(driver.getAddressDetail())
                .district(driver.getDistrict())
                .pay(driver.getPay())
                .companyJoin(driver.getCompanyJoin())
                .driverType(license != null ? license.getType() : null)
                .createdAt(DateUtils.format(driver.getCreatedAt()))
                .updatedAt(DateUtils.format(driver.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<Void> retiredDriver(UserPrincipal userPrincipal, Long driverId) {
        User user = userRepository.findByUsername(userPrincipal.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        if (driver.getStatus() == DriverStatus.RETIRED) {
            return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        }

        driver.setStatus(DriverStatus.RETIRED);

        deleteLogService.createLog(TableRef.DRIVER, driverId, user);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    private GetAllDriverResponseDto toGetAllDriverResponseDto(Driver driver) {
        return GetAllDriverResponseDto.builder()
                .driverId(driver.getId())
                .name(driver.getName())
                .status(driver.getStatus())
                .phoneNumber(driver.getPhoneNumber())
                .createdAt(DateUtils.format(driver.getCreatedAt()))
                .updatedAt(DateUtils.format(driver.getUpdatedAt()))
                .build();
    }

    private DriverUpdateLog buildUpdateLog(Driver driver, User user, String type, String prevData, String newData) {
        return DriverUpdateLog.builder()
                .driver(driver)
                .user(user)
                .changedByUsername(user.getUsername())
                .type(type)
                .prevData(prevData)
                .newData(newData)
                .build();
    }
}
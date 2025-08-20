package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.constants.ResponseCode;
import com.logi_flow.backend.common.constants.ResponseMessage;
import com.logi_flow.backend.common.enums.user.UserRole;
import com.logi_flow.backend.common.enums.user.UserStatus;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.driver.request.CreateDriverRequestDto;
import com.logi_flow.backend.dto.driver.request.UpdateDriverRequestDto;
import com.logi_flow.backend.dto.driver.response.CreateDriverResponseDto;
import com.logi_flow.backend.dto.driver.response.GetAllDriverResponseDto;
import com.logi_flow.backend.dto.driver.response.GetDriverDetailResponseDto;
import com.logi_flow.backend.dto.driver.response.UpdateDriverResponseDto;
import com.logi_flow.backend.entity.Driver;
import com.logi_flow.backend.entity.Role;
import com.logi_flow.backend.entity.User;
import com.logi_flow.backend.repository.DriverRepository;
import com.logi_flow.backend.repository.RoleRepository;
import com.logi_flow.backend.repository.UserRepository;
import com.logi_flow.backend.service.DriverService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseDto<CreateDriverResponseDto> createDriver(CreateDriverRequestDto dto) {
        CreateDriverResponseDto data = null;

        if(driverRepository.findByPhoneNumber(dto.getPhoneNumber()).isPresent())
            throw new IllegalArgumentException(ResponseMessage.ALREADY_EXISTS);

        long driverCount = driverRepository.count();
        String username = String.format("driver%03d", driverCount + 1);

        Role role = roleRepository.findByName(UserRole.DRIVER)
                .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.RESOURCE_NOT_FOUND));

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
                .build();

        userRepository.save(newUser);

        Driver newDriver = Driver.builder()
                .user(newUser)
                .status(dto.getStatus())
                .name(dto.getName())
                .identityNumber(dto.getIdentityNumber())
                .phoneNumber(dto.getPhoneNumber())
                .zipcode(dto.getZipcode())
                .address(dto.getAddress())
                .addressDetail(dto.getAddressDetail())
                .pay(dto.getPay())
                .companyJoin(dto.getCompanyJoin())
                .build();

        driverRepository.save(newDriver);

        data = CreateDriverResponseDto.builder()
                .driverId(newDriver.getId())
                .name(newDriver.getName())
                .username(newDriver.getUser().getUsername())
                .password(newDriver.getUser().getPassword())
                .createdAt(newDriver.getCreatedAt())
                .updatedAt(newDriver.getUpdatedAt())
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<UpdateDriverResponseDto> updateDriver(Long driverId, UpdateDriverRequestDto dto) {
        UpdateDriverResponseDto data = null;

        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        if (dto.getStatus() != null && !driver.getStatus().equals(dto.getStatus())) {
            driver.setStatus(dto.getStatus());
        }
        if (dto.getName() != null && !driver.getName().equals(dto.getName())) {
            driver.setName(dto.getName());
        }
        if (dto.getPhoneNumber() != null && !driver.getPhoneNumber().equals(dto.getPhoneNumber())) {
            driver.setPhoneNumber(dto.getPhoneNumber());
        }
        if (dto.getZipcode() != null && !driver.getZipcode().equals(dto.getZipcode())) {
            driver.setZipcode(dto.getZipcode());
        }
        if (dto.getAddress() != null && !driver.getAddress().equals(dto.getAddress())) {
            driver.setAddress(dto.getAddress());
        }
        if (dto.getAddressDetail() != null && !driver.getAddressDetail().equals(dto.getAddressDetail())) {
            driver.setAddressDetail(dto.getAddressDetail());
        }
        if (dto.getDistrict() != null && !driver.getDistrict().equals(dto.getDistrict())) {
            driver.setDistrict(dto.getDistrict());
        }
        if (dto.getPay() != null && !driver.getPay().equals(dto.getPay())) {
            driver.setPay(dto.getPay());
        }

        driverRepository.save(driver);

        data = UpdateDriverResponseDto.builder()
                .driverId(driver.getId())
                .name(driver.getName())
                .createdAt(driver.getCreatedAt())
                .updatedAt(driver.getUpdatedAt())
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<List<GetAllDriverResponseDto>> getAllDriver() {
        List<GetAllDriverResponseDto> data = null;

        List<Driver> drivers = driverRepository.findAll();

        data = drivers.stream()
                .map(driver -> GetAllDriverResponseDto.builder()
                        .driverId(driver.getId())
                        .name(driver.getName())
                        .status(driver.getStatus())
                        .phoneNumber(driver.getPhoneNumber())
                        .createdAt(driver.getCreatedAt())
                        .updatedAt(driver.getUpdatedAt())
                        .build()).collect(Collectors.toList());

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<GetDriverDetailResponseDto> getDriverDetail(Long driverId) {
        GetDriverDetailResponseDto data = null;

        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

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
                .driverType(driver.getDriverLicense().getType())
                .createdAt(driver.getCreatedAt())
                .updatedAt(driver.getUpdatedAt())
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<?> deleteDriver(Long driverId) {
        return null;
    }
}

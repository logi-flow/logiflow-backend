package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.constants.ResponseCode;
import com.logi_flow.backend.common.constants.ResponseMessage;
import com.logi_flow.backend.common.util.DateUtils;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.driver.request.CreateDriverLicenseRequestDto;
import com.logi_flow.backend.dto.driver.request.UpdateDriverLicenseRequestDto;
import com.logi_flow.backend.dto.driver.response.CreateDriverLicenseResponseDto;
import com.logi_flow.backend.dto.driver.response.UpdateDriverLicenseResponseDto;
import com.logi_flow.backend.entity.Driver;
import com.logi_flow.backend.entity.DriverLicense;
import com.logi_flow.backend.entity.DriverLicenseLog;
import com.logi_flow.backend.entity.User;
import com.logi_flow.backend.repository.DriverLicenseLogRepository;
import com.logi_flow.backend.repository.DriverLicenseRepository;
import com.logi_flow.backend.repository.DriverRepository;
import com.logi_flow.backend.repository.UserRepository;
import com.logi_flow.backend.service.DriverLicenseService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class DriverLicenseServiceImpl implements DriverLicenseService {

    private final DriverLicenseRepository driverLicenseRepository;
    private final DriverRepository driverRepository;
    private final DriverLicenseLogRepository driverLicenseLogRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseDto<CreateDriverLicenseResponseDto> createDriverLicense(Long driverId, CreateDriverLicenseRequestDto dto) {
        CreateDriverLicenseResponseDto data = null;

        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        DriverLicense newDriverLicense = DriverLicense.builder()
                .driver(driver)
                .driverNumber(dto.getDriverNumber())
                .type(dto.getType())
                .expiredDate(dto.getExpiredDate())
                .build();

        driverLicenseRepository.save(newDriverLicense);

        data = CreateDriverLicenseResponseDto.builder()
                .driverLicenseId(newDriverLicense.getId())
                .name(newDriverLicense.getDriver().getName())
                .driverNumber(newDriverLicense.getDriverNumber())
                .expiredDate(newDriverLicense.getExpiredDate())
                .createdAt(DateUtils.format(newDriverLicense.getCreatedAt()))
                .updatedAt(DateUtils.format(newDriverLicense.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<UpdateDriverLicenseResponseDto> updateDriverLicense(UserPrincipal userPrincipal, Long driverId, Long licenseId, UpdateDriverLicenseRequestDto dto) {
        UpdateDriverLicenseResponseDto data = null;

        User user = userRepository.findByUsername(userPrincipal.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        DriverLicense driverLicense = driverLicenseRepository.findById(licenseId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        if (driverLicense.getDriver().getId().equals(driver.getId())) {
            return ResponseDto.fail(ResponseCode.FAILED, ResponseMessage.FAILED);
        }

        if (dto.getType() != null && !driverLicense.getType().equals(dto.getType())) {
            String prevData = String.valueOf(driverLicense.getType());
            driverLicense.setType(dto.getType());
            createUpdateLog(driverLicense, user, "license_type", prevData, String.valueOf(driverLicense.getType()));
        }

        if(dto.getExpiredDate() != null && !driverLicense.getExpiredDate().equals(dto.getExpiredDate())) {
            String prevData = String.valueOf(driverLicense.getExpiredDate());
            driverLicense.setExpiredDate(dto.getExpiredDate());
            createUpdateLog(driverLicense, user, "expiredDate", prevData, String.valueOf(driverLicense.getExpiredDate()));
        }

        driverLicenseRepository.save(driverLicense);

        data = UpdateDriverLicenseResponseDto.builder()
                .driverLicenseId(driverLicense.getId())
                .name(driverLicense.getDriver().getName())
                .driverNumber(driverLicense.getDriverNumber())
                .expiredDate(driverLicense.getExpiredDate())
                .createdAt(DateUtils.format(driverLicense.getCreatedAt()))
                .updatedAt(DateUtils.format(driverLicense.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    private void createUpdateLog(DriverLicense driverLicense, User user, String type, String prevData, String newData) {
        DriverLicenseLog driverLicenseLog = DriverLicenseLog.builder()
                .driverLicense(driverLicense)
                .user(user)
                .changedByUsername(user.getUsername())
                .type(type)
                .prevData(prevData)
                .newData(newData)
                .build();

        driverLicenseLogRepository.save(driverLicenseLog);
    }
}

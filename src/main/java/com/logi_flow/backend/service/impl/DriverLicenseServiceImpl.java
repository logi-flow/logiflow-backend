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
import com.logi_flow.backend.entity.*;
import com.logi_flow.backend.repository.DriverLicenseLogRepository;
import com.logi_flow.backend.repository.DriverLicenseRepository;
import com.logi_flow.backend.repository.DriverRepository;
import com.logi_flow.backend.repository.UserRepository;
import com.logi_flow.backend.service.AlertService;
import com.logi_flow.backend.service.DriverLicenseService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class DriverLicenseServiceImpl implements DriverLicenseService {

    private final DriverLicenseRepository driverLicenseRepository;
    private final DriverRepository driverRepository;
    private final DriverLicenseLogRepository driverLicenseLogRepository;
    private final UserRepository userRepository;
    private final AlertService alertService;

    @Override
    public ResponseDto<CreateDriverLicenseResponseDto> createDriverLicense(Long driverId, CreateDriverLicenseRequestDto dto) {
        CreateDriverLicenseResponseDto data = null;

        LocalDate expiredDate = dto.getExpiredDate();
        LocalDate today = LocalDate.now();

        if (expiredDate.isBefore(today)) {
            throw new IllegalArgumentException(ResponseMessage.OUT_OF_TIME);
        }

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

        LocalDate expiredDate = dto.getExpiredDate();
        LocalDate today = LocalDate.now();

        if (expiredDate.isBefore(today)) {
            throw new IllegalArgumentException(ResponseMessage.OUT_OF_TIME);
        }

        User user = userRepository.findByUsername(userPrincipal.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        DriverLicense driverLicense = driverLicenseRepository.findById(licenseId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        if (!driverLicense.getDriver().getId().equals(driver.getId())) {
            return ResponseDto.fail(ResponseCode.NOT_MATCH, ResponseMessage.FAILED);
        }

        List<DriverLicenseLog> logs = new ArrayList<>();

        if (dto.getType() != null && !driverLicense.getType().equals(dto.getType())) {
            logs.add(buildUpdateLog(driverLicense, user, "license_type", String.valueOf(driverLicense.getType()), String.valueOf(driverLicense.getType())));
            driverLicense.setType(dto.getType());
        }

        if(dto.getExpiredDate() != null && !driverLicense.getExpiredDate().equals(dto.getExpiredDate())) {
            logs.add(buildUpdateLog(driverLicense, user, "expiredDate", String.valueOf(driverLicense.getExpiredDate()), String.valueOf(driverLicense.getExpiredDate())));
            driverLicense.setExpiredDate(dto.getExpiredDate());
        }

        driverLicenseRepository.save(driverLicense);
        driverLicenseLogRepository.saveAll(logs);

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

    @Override
    public void noticeExpiredDate() {
        LocalDate today = LocalDate.now();
        LocalDate oneMonthLater = today.plusMonths(1);

        List<DriverLicense> expLicenses = driverLicenseRepository.findByExpiredDateBetween(today, oneMonthLater);

        for (DriverLicense driverLicense : expLicenses) {
            Driver driver = driverLicense.getDriver();
            if (driver != null) {
                String alertMessage = "운전면허증 만료일이 1달 남았습니다. 확인해주세요!";
                alertService.sendToUser(driver.getUser().getId(), alertMessage);
            }
        }
    }

    private DriverLicenseLog buildUpdateLog(DriverLicense driverLicense, User user, String type, String prevData, String newData) {
        return DriverLicenseLog.builder()
                .driverLicense(driverLicense)
                .user(user)
                .changedByUsername(user.getUsername())
                .type(type)
                .prevData(prevData)
                .newData(newData)
                .build();
    }
}

package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.constants.ResponseCode;
import com.logi_flow.backend.common.constants.ResponseMessage;
import com.logi_flow.backend.common.enums.DriverPayrollStatus;
import com.logi_flow.backend.common.enums.TableRef;
import com.logi_flow.backend.common.util.DateUtils;
import com.logi_flow.backend.common.util.SortUtils;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.driverPayroll.request.CreateDriverPayrollRequestDto;
import com.logi_flow.backend.dto.driverPayroll.request.UpdateDriverPayrollRequestDto;
import com.logi_flow.backend.dto.driverPayroll.request.UpdateDriverPayrollStatusRequestDto;
import com.logi_flow.backend.dto.driverPayroll.response.*;
import com.logi_flow.backend.entity.*;
import com.logi_flow.backend.repository.*;
import com.logi_flow.backend.service.DeleteLogService;
import com.logi_flow.backend.service.DriverPayrollService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DriverPayrollServiceImpl implements DriverPayrollService {

    private final DriverPayrollRepository driverPayrollRepository;
    private final DriverRepository driverRepository;
    private final UserRepository userRepository;
    private final DriverPayrollStatusLogRepository driverPayrollStatusLogRepository;
    private final DriverPayrollUpdateLogRepository driverPayrollUpdateLogRepository;
    private final DeleteLogService deleteLogService;

    @Override
    @Transactional
    public ResponseDto<CreateDriverPayrollResponseDto> createDriverPayroll(CreateDriverPayrollRequestDto dto) {
        CreateDriverPayrollResponseDto data = null;

        Driver driver = driverRepository.findById(dto.getDriverId())
                .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.USER_NOT_FOUND));

        Optional<DriverPayroll> deletedDriverPayroll = driverPayrollRepository.findByDriverIdAndPeriodStartDateAndPeriodEndDateAndStatus(dto.getDriverId(), dto.getPeriodStartDate(), dto.getPeriodEndDate(), DriverPayrollStatus.DELETED);

        if (deletedDriverPayroll.isPresent()) {
            DriverPayroll restoreDriverPayroll = deletedDriverPayroll.get();
            restoreDriverPayroll.setTitle(dto.getTitle());
            restoreDriverPayroll.setStatus(DriverPayrollStatus.CREATED);
            DriverPayroll savedDriverPayroll = driverPayrollRepository.save(restoreDriverPayroll);

            deleteLogService.removeIfExists(TableRef.DRIVER_PAYROLL, savedDriverPayroll.getId());

            data = toCreateDriverPayrollResponseDto(savedDriverPayroll);

            return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
        }

        DriverPayroll newDriverPayroll = DriverPayroll.builder()
                .driver(driver)
                .status(DriverPayrollStatus.CREATED)
                .title(dto.getTitle())
                .periodStartDate(dto.getPeriodStartDate())
                .periodEndDate(dto.getPeriodEndDate())
                .build();
        DriverPayroll savedDriverPayroll = driverPayrollRepository.save(newDriverPayroll);

        data = toCreateDriverPayrollResponseDto(savedDriverPayroll);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GetAllDriverPayrollResponseDto> getAllDriverPayroll(int page, int size, String sort) {
        Page<GetAllDriverPayrollResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<DriverPayroll> payrolls = driverPayrollRepository.findAll(pageable);

        data = payrolls.map(this::toGetAllDriverPayrollResponseDto);

        return data;
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseDto<GetDriverPayrollDetailResponseDto> getDriverPayrollDetail(Long payrollId) {
        GetDriverPayrollDetailResponseDto data = null;
        DriverPayroll savedDriverPayroll = getDriverPayroll(payrollId);

        data = GetDriverPayrollDetailResponseDto.builder()
                .driverId(savedDriverPayroll.getDriver().getId())
                .driverName(savedDriverPayroll.getDriver().getName())
                .title(savedDriverPayroll.getTitle())
                .periodStartDate(savedDriverPayroll.getPeriodStartDate())
                .periodEndDate(savedDriverPayroll.getPeriodEndDate())
                .totalAllowance(savedDriverPayroll.getTotalAllowance())
                .totalDeduction(savedDriverPayroll.getTotalDeduction())
                .finalAmount(savedDriverPayroll.getFinalAmount())
                .status(savedDriverPayroll.getStatus().name())
                .createdAt(DateUtils.format(savedDriverPayroll.getCreatedAt()))
                .updatedAt(DateUtils.format(savedDriverPayroll.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GetAllDriverPayrollResponseDto> getMyPayrolls(UserPrincipal userPrincipal, int page, int size, String sort) {
        Page<GetAllDriverPayrollResponseDto> data = null;

        User user = userRepository.findByUsername(userPrincipal.getUsername())
                .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.USER_NOT_FOUND));

        Driver driver = driverRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.USER_NOT_FOUND));

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<DriverPayroll> payrolls = driverPayrollRepository.findByDriverIdAndStatus(driver.getId(), DriverPayrollStatus.CONFIRMED, pageable);

        data = payrolls.map(this::toGetAllDriverPayrollResponseDto);

        return data;
    }

    @Override
    @Transactional
    public ResponseDto<UpdateDriverPayrollStatusResponseDto> updateDriverPayrollStatus(UserPrincipal userPrincipal, Long payrollId, UpdateDriverPayrollStatusRequestDto dto) {
        UpdateDriverPayrollStatusResponseDto data = null;

        User user = userRepository.findByUsername(userPrincipal.getUsername())
                .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.USER_NOT_FOUND));

        DriverPayroll savedDriverPayroll = getDriverPayroll(payrollId);
        DriverPayrollStatus prevStatus = savedDriverPayroll.getStatus();

        savedDriverPayroll.setStatus(dto.getStatus());
        DriverPayroll updatedDriverPayroll = driverPayrollRepository.save(savedDriverPayroll);

        createStatusLog(user, updatedDriverPayroll, dto.getChangeReason(), prevStatus);

        data = UpdateDriverPayrollStatusResponseDto.builder()
                .id(updatedDriverPayroll.getId())
                .driverId(updatedDriverPayroll.getDriver().getId())
                .title(updatedDriverPayroll.getTitle())
                .periodStartDate(updatedDriverPayroll.getPeriodStartDate())
                .periodEndDate(updatedDriverPayroll.getPeriodEndDate())
                .totalAllowance(updatedDriverPayroll.getTotalAllowance())
                .totalDeduction(updatedDriverPayroll.getTotalDeduction())
                .finalAmount(updatedDriverPayroll.getFinalAmount())
                .status(updatedDriverPayroll.getStatus())
                .changeReason(dto.getChangeReason())
                .createdAt(DateUtils.format(savedDriverPayroll.getCreatedAt()))
                .updatedAt(DateUtils.format(savedDriverPayroll.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<UpdateDriverPayrollResponseDto> updateDriverPayroll(UserPrincipal userPrincipal, Long payrollId, UpdateDriverPayrollRequestDto dto) {
        UpdateDriverPayrollResponseDto data = null;

        User user = userRepository.findByUsername(userPrincipal.getUsername())
                .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.USER_NOT_FOUND));

        DriverPayroll savedDriverPayroll = getDriverPayroll(payrollId);

        if (!Objects.equals(dto.getTitle(), savedDriverPayroll.getTitle())) {
            String prevData = savedDriverPayroll.getTitle();
            savedDriverPayroll.setTitle(dto.getTitle());
            createUpdateLog(user, savedDriverPayroll, "title", prevData, savedDriverPayroll.getTitle());
        }

        if (!dto.getPeriodStartDate().equals(savedDriverPayroll.getPeriodStartDate())) {
            String prevData = savedDriverPayroll.getPeriodStartDate().toString();
            savedDriverPayroll.setPeriodStartDate(dto.getPeriodStartDate());
            createUpdateLog(user, savedDriverPayroll, "period_start_date", prevData, savedDriverPayroll.getPeriodStartDate().toString());
        }

        if (!dto.getPeriodEndDate().equals(savedDriverPayroll.getPeriodEndDate())) {
            String prevData = savedDriverPayroll.getPeriodEndDate().toString();
            savedDriverPayroll.setPeriodEndDate(dto.getPeriodEndDate());
            createUpdateLog(user, savedDriverPayroll, "period_end_date", prevData, savedDriverPayroll.getPeriodEndDate().toString());
        }

        DriverPayroll updatedDriverPayroll = driverPayrollRepository.save(savedDriverPayroll);

        data = UpdateDriverPayrollResponseDto.builder()
                .id(updatedDriverPayroll.getId())
                .driverId(updatedDriverPayroll.getDriver().getId())
                .title(updatedDriverPayroll.getTitle())
                .periodStartDate(updatedDriverPayroll.getPeriodStartDate())
                .periodEndDate(updatedDriverPayroll.getPeriodEndDate())
                .totalAllowance(updatedDriverPayroll.getTotalAllowance())
                .totalDeduction(updatedDriverPayroll.getTotalDeduction())
                .finalAmount(updatedDriverPayroll.getFinalAmount())
                .createdAt(DateUtils.format(savedDriverPayroll.getCreatedAt()))
                .updatedAt(DateUtils.format(savedDriverPayroll.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    @Transactional
    public ResponseDto<Void> deleteDriverPayroll(UserPrincipal userPrincipal, Long payrollId) {
        User user = userRepository.findByUsername(userPrincipal.getUsername())
                .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.USER_NOT_FOUND));

        DriverPayroll savedDriverPayroll = getDriverPayroll(payrollId);

        if (savedDriverPayroll.getStatus() == DriverPayrollStatus.DELETED) {
            return ResponseDto.fail(ResponseCode.ALREADY_DELETED, ResponseMessage.ALREADY_DELETED);
        }

        savedDriverPayroll.setStatus(DriverPayrollStatus.DELETED);
        driverPayrollRepository.save(savedDriverPayroll);

        deleteLogService.createLog(TableRef.DRIVER_PAYROLL, payrollId, user);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    private DriverPayroll getDriverPayroll(Long payrollId) {
        return driverPayrollRepository.findById(payrollId)
                .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.RESOURCE_NOT_FOUND));
    }

    private CreateDriverPayrollResponseDto toCreateDriverPayrollResponseDto(DriverPayroll driverPayroll) {
        return CreateDriverPayrollResponseDto.builder()
                .id(driverPayroll.getId())
                .driverId(driverPayroll.getDriver().getId())
                .title(driverPayroll.getTitle())
                .periodStartDate(driverPayroll.getPeriodStartDate())
                .periodEndDate(driverPayroll.getPeriodEndDate())
                .totalAllowance(driverPayroll.getTotalAllowance())
                .totalDeduction(driverPayroll.getTotalDeduction())
                .finalAmount(driverPayroll.getFinalAmount())
                .createdAt(DateUtils.format(driverPayroll.getCreatedAt()))
                .updatedAt(DateUtils.format(driverPayroll.getUpdatedAt()))
                .build();
    }

    private GetAllDriverPayrollResponseDto toGetAllDriverPayrollResponseDto(DriverPayroll driverPayroll) {
        return GetAllDriverPayrollResponseDto.builder()
                .driverId(driverPayroll.getDriver().getId())
                .driverName(driverPayroll.getDriver().getName())
                .title(driverPayroll.getTitle())
                .totalAllowance(driverPayroll.getTotalAllowance())
                .totalDeduction(driverPayroll.getTotalDeduction())
                .finalAmount(driverPayroll.getFinalAmount())
                .createdAt(DateUtils.format(driverPayroll.getCreatedAt()))
                .updatedAt(DateUtils.format(driverPayroll.getUpdatedAt()))
                .build();
    }

    private void createStatusLog(User user, DriverPayroll updatedDriverPayroll, String changeReason, DriverPayrollStatus prevStatus) {
        DriverPayrollStatusLog driverPayrollStatusLog = DriverPayrollStatusLog.builder()
                .driverPayroll(updatedDriverPayroll)
                .user(user)
                .changedByUsername(user.getUsername())
                .changeReason(changeReason)
                .prevStatus(prevStatus)
                .newStatus(updatedDriverPayroll.getStatus())
                .build();

        driverPayrollStatusLogRepository.save(driverPayrollStatusLog);
    }

    private void createUpdateLog(User user, DriverPayroll updatedDriverPayroll, String type, String prevData, String newData) {
        DriverPayrollUpdateLog driverPayrollUpdateLog = DriverPayrollUpdateLog.builder()
                .driverPayroll(updatedDriverPayroll)
                .user(user)
                .changedByUsername(user.getUsername())
                .type(type)
                .prevData(prevData)
                .newData(newData)
                .build();

        driverPayrollUpdateLogRepository.save(driverPayrollUpdateLog);
    }
}

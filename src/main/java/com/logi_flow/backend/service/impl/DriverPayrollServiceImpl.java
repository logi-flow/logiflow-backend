package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.constants.ResponseCode;
import com.logi_flow.backend.common.constants.ResponseMessage;
import com.logi_flow.backend.common.enums.DriverAllowanceStatus;
import com.logi_flow.backend.common.enums.DriverPayrollStatus;
import com.logi_flow.backend.common.enums.TableRef;
import com.logi_flow.backend.common.mapper.DriverPayrollMapper;
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
import com.logi_flow.backend.service.AlertService;
import com.logi_flow.backend.service.DeleteLogService;
import com.logi_flow.backend.service.DriverPayrollService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DriverPayrollServiceImpl implements DriverPayrollService {

    private final DriverPayrollRepository driverPayrollRepository;
    private final DriverRepository driverRepository;
    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;
    private final AllowanceTypeRepository allowanceTypeRepository;
    private final DriverAllowanceRepository driverAllowanceRepository;
    private final DriverDeductionRepository driverDeductionRepository;
    private final DriverPayrollStatusLogRepository driverPayrollStatusLogRepository;
    private final DriverPayrollUpdateLogRepository driverPayrollUpdateLogRepository;
    private final DeleteLogService deleteLogService;
    private final AlertService alertService;
    private final DriverPayrollMapper driverPayrollMapper;

    @Override
    @Transactional
    public ResponseDto<CreateDriverPayrollResponseDto> createDriverPayroll(CreateDriverPayrollRequestDto dto) {
        CreateDriverPayrollResponseDto data = null;

        Driver driver = driverRepository.findById(dto.getDriverId())
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        if (driverPayrollRepository.lockAnyActiveOverlapId(dto.getDriverId(), dto.getPeriodStartDate(), dto.getPeriodEndDate()).isPresent()) {
            throw new DataIntegrityViolationException(ResponseMessage.EXISTS_PAYROLL);
        }

        DriverPayroll driverPayroll = driverPayrollRepository.findByDriverIdAndPeriodStartDateAndPeriodEndDateAndStatus(dto.getDriverId(), dto.getPeriodStartDate(), dto.getPeriodEndDate(), DriverPayrollStatus.DELETED)
                .map(p -> {
                    p.setTitle(dto.getTitle());
                    p.setStatus(DriverPayrollStatus.CREATED);
                    return p;
                })
                .orElseGet(() -> DriverPayroll.builder()
                        .driver(driver)
                        .status(DriverPayrollStatus.CREATED)
                        .title(dto.getTitle())
                        .periodStartDate(dto.getPeriodStartDate())
                        .periodEndDate(dto.getPeriodEndDate())
                        .build());

        DriverPayroll savedDriverPayroll = driverPayrollRepository.save(driverPayroll);

        deleteLogService.removeIfExists(TableRef.DRIVER_PAYROLL, savedDriverPayroll.getId());

        int workDays = attendanceRepository.countWorkDays(driver.getId(), dto.getPeriodStartDate(), dto.getPeriodEndDate());
        AllowanceType baseType = allowanceTypeRepository.findByCode("BASE")
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        DriverAllowance base = driverAllowanceRepository.findByDriverPayrollIdAndAllowanceTypeIdAndStatus(savedDriverPayroll.getId(), baseType.getId(), DriverAllowanceStatus.ACTIVE)
                .orElseGet(() ->  DriverAllowance.builder()
                        .driverPayroll(savedDriverPayroll)
                        .allowanceType(baseType)
                        .status(DriverAllowanceStatus.ACTIVE)
                        .build());

        base.setQuantity(new BigDecimal(workDays));
        base.setUnitPrice(driver.getPay());
        base.setMemo(workDays + "(일) × " + driver.getPay() + "(원)");
        driverAllowanceRepository.save(base);

        int totalAllowance = driverAllowanceRepository.sumAmountByDriverPayrollId(savedDriverPayroll.getId());
        int totalDeduction = driverDeductionRepository.sumAmountByDriverPayrollId(savedDriverPayroll.getId());

        savedDriverPayroll.applyTotals(totalAllowance, totalDeduction);

        data = toCreateDriverPayrollResponseDto(savedDriverPayroll);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public Page<GetAllDriverPayrollResponseDto> getAllDriverPayroll(int page, int size, String sort) {
        Page<GetAllDriverPayrollResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<DriverPayroll> payrolls = driverPayrollRepository.findByStatusNot(DriverPayrollStatus.DELETED, pageable);

        data = payrolls.map(this::toGetAllDriverPayrollResponseDto);

        return data;
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseDto<GetDriverPayrollDetailResponseDto> getDriverPayrollDetail(Long payrollId) {
        GetDriverPayrollDetailResponseDto data = null;

        data = driverPayrollMapper.getDriverPayrollDetail(payrollId);

        if (data == null) {
            throw new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND);
        }

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public Page<GetAllDriverPayrollResponseDto> getMyPayrolls(UserPrincipal userPrincipal, int page, int size, String sort) {
        Page<GetAllDriverPayrollResponseDto> data = null;

        Driver driver = driverRepository.findByUserId(userPrincipal.getId())
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<DriverPayroll> payrolls = driverPayrollRepository.findByDriverIdAndStatus(driver.getId(), DriverPayrollStatus.CONFIRMED, pageable);

        data = payrolls.map(this::toGetAllDriverPayrollResponseDto);

        return data;
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseDto<GetDriverPayrollDetailResponseDto> getMyPayrollDetail(UserPrincipal userPrincipal, Long payrollId) throws AccessDeniedException {
        GetDriverPayrollDetailResponseDto data = null;

        Driver driver = driverRepository.findByUserId(userPrincipal.getId())
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        data = driverPayrollMapper.getDriverPayrollDetail(payrollId);

        if (data == null) {
            throw new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND);
        }

        if (!driver.getId().equals(data.getDriverId())) {
            throw new AccessDeniedException(ResponseMessage.NOT_OWN_PAYROLL);
        }

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    @Transactional
    public ResponseDto<UpdateDriverPayrollStatusResponseDto> updateDriverPayrollStatus(UserPrincipal userPrincipal, Long payrollId, UpdateDriverPayrollStatusRequestDto dto) {
        UpdateDriverPayrollStatusResponseDto data = null;

        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        DriverPayroll savedDriverPayroll = getDriverPayroll(payrollId);
        DriverPayrollStatus prevStatus = savedDriverPayroll.getStatus();

        savedDriverPayroll.setStatus(dto.getStatus());
        DriverPayroll updatedDriverPayroll = driverPayrollRepository.save(savedDriverPayroll);

        createStatusLog(user, updatedDriverPayroll, dto.getChangeReason(), prevStatus);

        String alertMessage = updatedDriverPayroll.getDriver().getName() + " 기사님의 급여대장이 확정되었습니다. 확인해 주세요.";

        if (updatedDriverPayroll.getStatus().equals(DriverPayrollStatus.CREATED)) {
            alertMessage = updatedDriverPayroll.getDriver().getName() + " 기사님의 급여대장이 확정 취소되었습니다. 잠시 후 확인해 주세요.";
        }

        alertService.sendToUser(updatedDriverPayroll.getDriver().getUser().getId(), alertMessage);

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
    @Transactional
    public ResponseDto<UpdateDriverPayrollResponseDto> updateDriverPayroll(UserPrincipal userPrincipal, Long payrollId, UpdateDriverPayrollRequestDto dto) {
        UpdateDriverPayrollResponseDto data = null;

        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        DriverPayroll savedDriverPayroll = getDriverPayroll(payrollId);

        if (savedDriverPayroll.getStatus() == DriverPayrollStatus.CONFIRMED) {
            return ResponseDto.fail(ResponseCode.LOCK_PAYROLL_CONFIRMED, ResponseMessage.LOCK_PAYROLL_CONFIRMED);
        } else if (savedDriverPayroll.getStatus() == DriverPayrollStatus.DELETED) {
            return ResponseDto.fail(ResponseCode.ALREADY_DELETED,ResponseMessage.ALREADY_DELETED);
        }

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
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        DriverPayroll savedDriverPayroll = getDriverPayroll(payrollId);

        if (savedDriverPayroll.getStatus() == DriverPayrollStatus.CONFIRMED) {
            return ResponseDto.fail(ResponseCode.LOCK_PAYROLL_CONFIRMED, ResponseMessage.LOCK_PAYROLL_CONFIRMED);
        } else if (savedDriverPayroll.getStatus() == DriverPayrollStatus.DELETED) {
            return ResponseDto.fail(ResponseCode.ALREADY_DELETED,ResponseMessage.ALREADY_DELETED);
        }

        savedDriverPayroll.setStatus(DriverPayrollStatus.DELETED);
        driverPayrollRepository.save(savedDriverPayroll);

        deleteLogService.createLog(TableRef.DRIVER_PAYROLL, payrollId, user);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    @Override
    public DriverPayroll getDriverPayroll(Long payrollId) {
        return driverPayrollRepository.findById(payrollId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));
    }

    @Override
    public DriverPayroll getDriverPayrollForUpdate(Long payrollId) {
        return driverPayrollRepository.findByIdForUpdate(payrollId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));
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
                .id(driverPayroll.getId())
                .driverId(driverPayroll.getDriver().getId())
                .driverName(driverPayroll.getDriver().getName())
                .title(driverPayroll.getTitle())
                .totalAllowance(driverPayroll.getTotalAllowance())
                .totalDeduction(driverPayroll.getTotalDeduction())
                .finalAmount(driverPayroll.getFinalAmount())
                .status(driverPayroll.getStatus())
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

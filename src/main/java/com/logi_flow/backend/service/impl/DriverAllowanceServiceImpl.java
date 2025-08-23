package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.constants.ResponseCode;
import com.logi_flow.backend.common.constants.ResponseMessage;
import com.logi_flow.backend.common.enums.AllowanceTypeStatus;
import com.logi_flow.backend.common.enums.DriverAllowanceStatus;
import com.logi_flow.backend.common.enums.DriverPayrollStatus;
import com.logi_flow.backend.common.enums.TableRef;
import com.logi_flow.backend.common.util.DateUtils;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.driverAllowance.request.CreateDriverAllowanceRequestDto;
import com.logi_flow.backend.dto.driverAllowance.request.UpdateDriverAllowanceRequestDto;
import com.logi_flow.backend.dto.driverAllowance.response.CreateDriverAllowanceResponseDto;
import com.logi_flow.backend.dto.driverAllowance.response.GetDriverAllowanceDetailResponseDto;
import com.logi_flow.backend.dto.driverAllowance.response.UpdateDriverAllowanceResponseDto;
import com.logi_flow.backend.entity.*;
import com.logi_flow.backend.repository.DriverAllowanceRepository;
import com.logi_flow.backend.repository.DriverAllowanceUpdateLogRepository;
import com.logi_flow.backend.repository.DriverDeductionRepository;
import com.logi_flow.backend.repository.UserRepository;
import com.logi_flow.backend.service.AllowanceTypeService;
import com.logi_flow.backend.service.DeleteLogService;
import com.logi_flow.backend.service.DriverAllowanceService;
import com.logi_flow.backend.service.DriverPayrollService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DriverAllowanceServiceImpl implements DriverAllowanceService {

    private final DriverAllowanceRepository driverAllowanceRepository;
    private final UserRepository userRepository;
    private final DriverDeductionRepository driverDeductionRepository;
    private final DriverAllowanceUpdateLogRepository driverAllowanceUpdateLogRepository;
    private final DriverPayrollService driverPayrollService;
    private final AllowanceTypeService allowanceTypeService;
    private final DeleteLogService deleteLogService;

    @Override
    @Transactional
    public ResponseDto<CreateDriverAllowanceResponseDto> createDriverAllowance(Long payrollId, CreateDriverAllowanceRequestDto dto) {
        CreateDriverAllowanceResponseDto data = null;

        int unitPrice = dto.getUnitPrice() == null ? 0 : dto.getUnitPrice();

        DriverPayroll payroll = driverPayrollService.getDriverPayrollForUpdate(payrollId);

        if (payroll.getStatus() == DriverPayrollStatus.CONFIRMED) {
            return ResponseDto.fail(ResponseCode.LOCK_PAYROLL_CONFIRMED, ResponseMessage.LOCK_PAYROLL_CONFIRMED);
        } else if (payroll.getStatus() == DriverPayrollStatus.DELETED) {
            return ResponseDto.fail(ResponseCode.ALREADY_DELETED,ResponseMessage.ALREADY_DELETED);
        }

        AllowanceType allowanceType = allowanceTypeService.getAllowanceType(dto.getAllowanceTypeId());

        if (allowanceType.getStatus() == AllowanceTypeStatus.DELETED) {
            return ResponseDto.fail(ResponseCode.ALREADY_DELETED,ResponseMessage.ALREADY_DELETED);
        }

        if (!allowanceType.isActive()) {
            return ResponseDto.fail(ResponseCode.NOT_USED_INACTIVE_TYPE, ResponseMessage.NOT_USED_INACTIVE_TYPE);
        }

        Optional<DriverAllowance> deletedDriverAllowance = driverAllowanceRepository.findByDriverPayrollIdAndAllowanceTypeIdAndStatus(payrollId, dto.getAllowanceTypeId(), DriverAllowanceStatus.DELETED);

        if (deletedDriverAllowance.isPresent()) {
            DriverAllowance restoreDriverAllowance = deletedDriverAllowance.get();
            restoreDriverAllowance.setQuantity(dto.getQuantity());
            restoreDriverAllowance.setUnitPrice(unitPrice);
            restoreDriverAllowance.setMemo(dto.getMemo());
            restoreDriverAllowance.setStatus(DriverAllowanceStatus.ACTIVE);
            DriverAllowance savedDriverAllowance = driverAllowanceRepository.save(restoreDriverAllowance);

            deleteLogService.removeIfExists(TableRef.DRIVER_ALLOWANCE, savedDriverAllowance.getId());

            int totalAllowance = driverAllowanceRepository.sumAmountByDriverPayrollId(payrollId);
            int totalDeduction = driverDeductionRepository.sumAmountByDriverPayrollId(payrollId);

            payroll.applyTotals(totalAllowance, totalDeduction);

            data = toCreateDriverAllowanceResponseDto(savedDriverAllowance);

            return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
        }

        DriverAllowance newDriverAllowance = DriverAllowance.builder()
                .driverPayroll(payroll)
                .allowanceType(allowanceType)
                .quantity(dto.getQuantity())
                .unitPrice(unitPrice)
                .memo(dto.getMemo())
                .status(DriverAllowanceStatus.ACTIVE)
                .build();

        DriverAllowance savedDriverAllowance = driverAllowanceRepository.save(newDriverAllowance);

        int totalAllowance = driverAllowanceRepository.sumAmountByDriverPayrollId(payrollId);
        int totalDeduction = driverDeductionRepository.sumAmountByDriverPayrollId(payrollId);

        payroll.applyTotals(totalAllowance, totalDeduction);

        data = toCreateDriverAllowanceResponseDto(savedDriverAllowance);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<List<GetDriverAllowanceDetailResponseDto>> getDriverAllowance(Long payrollId) {
        List<GetDriverAllowanceDetailResponseDto> data = null;

        List<DriverAllowance> driverAllowances = driverAllowanceRepository.findByDriverPayrollIdAndStatus(payrollId, DriverAllowanceStatus.ACTIVE);

        if (driverAllowances.isEmpty()) {
            throw new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND);
        }

        data = driverAllowances.stream()
                .map(this::toGetDriverAllowanceDetailResponseDto)
                .collect(Collectors.toList());

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    @Transactional
    public ResponseDto<List<UpdateDriverAllowanceResponseDto>> updateDriverAllowance(UserPrincipal userPrincipal, Long payrollId, List<UpdateDriverAllowanceRequestDto.Item> items) {
        List<UpdateDriverAllowanceResponseDto> data = new ArrayList<>(items.size());

        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        DriverPayroll payroll = driverPayrollService.getDriverPayrollForUpdate(payrollId);

        if (payroll.getStatus() == DriverPayrollStatus.CONFIRMED) {
            return ResponseDto.fail(ResponseCode.LOCK_PAYROLL_CONFIRMED, ResponseMessage.LOCK_PAYROLL_CONFIRMED);
        } else if (payroll.getStatus() == DriverPayrollStatus.DELETED) {
            return ResponseDto.fail(ResponseCode.ALREADY_DELETED,ResponseMessage.ALREADY_DELETED);
        }

        Set<Long> ids = items.stream()
                .map(UpdateDriverAllowanceRequestDto.Item::getId)
                .collect(Collectors.toSet());
        List<DriverAllowance> allowances = driverAllowanceRepository.findAllByIdInAndDriverPayrollId(ids, payrollId);
        Map<Long, DriverAllowance> byId = allowances.stream().collect(Collectors.toMap(DriverAllowance::getId, a -> a));

        if (byId.size() != ids.size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.RESOURCE_NOT_FOUND);
        }

        for (UpdateDriverAllowanceRequestDto.Item item : items) {
            DriverAllowance driverAllowance = byId.get(item.getId());

            if (item.getQuantity() != null && item.getQuantity().compareTo(driverAllowance.getQuantity()) != 0) {
                String prevData = driverAllowance.getQuantity().toString();
                driverAllowance.setQuantity(item.getQuantity());
                createUpdateLog(user, driverAllowance, "quantity", prevData, item.getQuantity().toString());
            }

            if (item.getUnitPrice() != null && !Objects.equals(item.getUnitPrice(), driverAllowance.getUnitPrice())) {
                String prevData = String.valueOf(driverAllowance.getUnitPrice());
                driverAllowance.setUnitPrice(item.getUnitPrice());
                createUpdateLog(user, driverAllowance, "unit_price", prevData, String.valueOf(item.getUnitPrice()));
            }

            if (item.getMemo() != null && !Objects.equals(item.getMemo(), driverAllowance.getMemo())) {
                String prevData = driverAllowance.getMemo();
                driverAllowance.setMemo(item.getMemo());
                createUpdateLog(user, driverAllowance, "memo", prevData, item.getMemo());
            }

            driverAllowance.calculateAmount();

            DriverAllowance updatedDriverAllowance = driverAllowanceRepository.save(driverAllowance);

            data.add(toUpdateDriverAllowanceResponseDto(updatedDriverAllowance));
        }

        int totalAllowance = driverAllowanceRepository.sumAmountByDriverPayrollId(payrollId);
        int totalDeduction = driverDeductionRepository.sumAmountByDriverPayrollId(payrollId);

        payroll.applyTotals(totalAllowance, totalDeduction);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    @Transactional
    public ResponseDto<Void> deleteDriverAllowance(UserPrincipal userPrincipal, Long payrollId, Long allowanceId) {
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        DriverPayroll payroll = driverPayrollService.getDriverPayrollForUpdate(payrollId);

        if (payroll.getStatus() == DriverPayrollStatus.CONFIRMED) {
            return ResponseDto.fail(ResponseCode.LOCK_PAYROLL_CONFIRMED, ResponseMessage.LOCK_PAYROLL_CONFIRMED);
        } else if (payroll.getStatus() == DriverPayrollStatus.DELETED) {
            return ResponseDto.fail(ResponseCode.ALREADY_DELETED,ResponseMessage.ALREADY_DELETED);
        }

        DriverAllowance driverAllowance = driverAllowanceRepository.findById(allowanceId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        if ("BASE".equals(driverAllowance.getAllowanceType().getCode())) {
            return ResponseDto.fail(ResponseCode.SYSTEM_ITEM_IMMUTABLE, ResponseMessage.SYSTEM_ITEM_IMMUTABLE);
        }

        if (driverAllowance.getStatus() == DriverAllowanceStatus.DELETED) {
            return ResponseDto.fail(ResponseCode.ALREADY_DELETED,ResponseMessage.ALREADY_DELETED);
        }

        driverAllowance.setStatus(DriverAllowanceStatus.DELETED);
        driverAllowanceRepository.save(driverAllowance);

        int totalAllowance = driverAllowanceRepository.sumAmountByDriverPayrollId(payrollId);
        int totalDeduction = driverDeductionRepository.sumAmountByDriverPayrollId(payrollId);

        payroll.applyTotals(totalAllowance, totalDeduction);

        deleteLogService.createLog(TableRef.DRIVER_ALLOWANCE, allowanceId, user);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    private CreateDriverAllowanceResponseDto toCreateDriverAllowanceResponseDto(DriverAllowance driverAllowance) {
        return CreateDriverAllowanceResponseDto.builder()
                .id(driverAllowance.getId())
                .allowanceTypeCode(driverAllowance.getAllowanceType().getCode())
                .allowanceTypeName(driverAllowance.getAllowanceType().getName())
                .quantity(driverAllowance.getQuantity())
                .unitPrice(driverAllowance.getUnitPrice())
                .amount(driverAllowance.getAmount())
                .memo(driverAllowance.getMemo())
                .status(driverAllowance.getStatus())
                .createdAt(DateUtils.format(driverAllowance.getCreatedAt()))
                .updatedAt(DateUtils.format(driverAllowance.getUpdatedAt()))
                .build();
    }

    private GetDriverAllowanceDetailResponseDto toGetDriverAllowanceDetailResponseDto(DriverAllowance driverAllowance) {
        return GetDriverAllowanceDetailResponseDto.builder()
                .id(driverAllowance.getId())
                .allowanceTypeId(driverAllowance.getAllowanceType().getId())
                .allowanceTypeCode(driverAllowance.getAllowanceType().getCode())
                .allowanceTypeName(driverAllowance.getAllowanceType().getName())
                .quantity(driverAllowance.getQuantity())
                .unitPrice(driverAllowance.getUnitPrice())
                .amount(driverAllowance.getAmount())
                .memo(driverAllowance.getMemo())
                .createdAt(DateUtils.format(driverAllowance.getCreatedAt()))
                .updatedAt(DateUtils.format(driverAllowance.getUpdatedAt()))
                .build();
    }

    private UpdateDriverAllowanceResponseDto toUpdateDriverAllowanceResponseDto(DriverAllowance driverAllowance) {
        return UpdateDriverAllowanceResponseDto.builder()
                .id(driverAllowance.getId())
                .allowanceTypeCode(driverAllowance.getAllowanceType().getCode())
                .allowanceTypeName(driverAllowance.getAllowanceType().getName())
                .quantity(driverAllowance.getQuantity())
                .unitPrice(driverAllowance.getUnitPrice())
                .amount(driverAllowance.getAmount())
                .memo(driverAllowance.getMemo())
                .createdAt(DateUtils.format(driverAllowance.getCreatedAt()))
                .updatedAt(DateUtils.format(driverAllowance.getUpdatedAt()))
                .build();
    }

    private void createUpdateLog(User user, DriverAllowance updatedDriverAllowance, String type, String prevData, String newData) {
        DriverAllowanceUpdateLog driverAllowanceUpdateLog = DriverAllowanceUpdateLog.builder()
                .driverAllowance(updatedDriverAllowance)
                .user(user)
                .changedByUsername(user.getUsername())
                .type(type)
                .prevData(prevData)
                .newData(newData)
                .build();

        driverAllowanceUpdateLogRepository.save(driverAllowanceUpdateLog);
    }
}

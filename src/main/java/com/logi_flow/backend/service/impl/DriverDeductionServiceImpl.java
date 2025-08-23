package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.constants.ResponseCode;
import com.logi_flow.backend.common.constants.ResponseMessage;
import com.logi_flow.backend.common.enums.*;
import com.logi_flow.backend.common.util.DateUtils;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.driverDeduction.request.CreateDriverDeductionRequestDto;
import com.logi_flow.backend.dto.driverDeduction.request.UpdateDriverDeductionRequestDto;
import com.logi_flow.backend.dto.driverDeduction.response.CreateDriverDeductionResponseDto;
import com.logi_flow.backend.dto.driverDeduction.response.GetDriverDeductionDetailResponseDto;
import com.logi_flow.backend.dto.driverDeduction.response.UpdateDriverDeductionResponseDto;
import com.logi_flow.backend.entity.*;
import com.logi_flow.backend.repository.*;
import com.logi_flow.backend.service.DeductionTypeService;
import com.logi_flow.backend.service.DeleteLogService;
import com.logi_flow.backend.service.DriverDeductionService;
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
public class DriverDeductionServiceImpl implements DriverDeductionService {

    private final DriverDeductionRepository driverDeductionRepository;
    private final UserRepository userRepository;
    private final DriverAllowanceRepository driverAllowanceRepository;
    private final DriverDeductionUpdateLogRepository driverDeductionUpdateLogRepository;
    private final DriverPayrollService driverPayrollService;
    private final DeductionTypeService deductionTypeService;
    private final DeleteLogService deleteLogService;

    @Override
    @Transactional
    public ResponseDto<CreateDriverDeductionResponseDto> createDriverDeduction(Long payrollId, CreateDriverDeductionRequestDto dto) {
        CreateDriverDeductionResponseDto data = null;

        int unitPrice = dto.getUnitPrice() == null ? 0 : dto.getUnitPrice();

        DriverPayroll payroll = driverPayrollService.getDriverPayrollForUpdate(payrollId);

        if (payroll.getStatus() == DriverPayrollStatus.CONFIRMED) {
            return ResponseDto.fail(ResponseCode.LOCK_PAYROLL_CONFIRMED, ResponseMessage.LOCK_PAYROLL_CONFIRMED);
        } else if (payroll.getStatus() == DriverPayrollStatus.DELETED) {
            return ResponseDto.fail(ResponseCode.ALREADY_DELETED,ResponseMessage.ALREADY_DELETED);
        }

        DeductionType deductionType = deductionTypeService.getDeductionType(dto.getDeductionTypeId());

        if (deductionType.getStatus() == DeductionTypeStatus.DELETED) {
            return ResponseDto.fail(ResponseCode.ALREADY_DELETED,ResponseMessage.ALREADY_DELETED);
        }

        if (!deductionType.isActive()) {
            return ResponseDto.fail(ResponseCode.NOT_USED_INACTIVE_TYPE, ResponseMessage.NOT_USED_INACTIVE_TYPE);
        }

        Optional<DriverDeduction> deletedDriverDeduction = driverDeductionRepository.findByDriverPayrollIdAndDeductionTypeIdAndStatus(payrollId, dto.getDeductionTypeId(), DriverDeductionStatus.DELETED);

        if (deletedDriverDeduction.isPresent()) {
            DriverDeduction restoreDriverDeduction = deletedDriverDeduction.get();
            restoreDriverDeduction.setQuantity(dto.getQuantity());
            restoreDriverDeduction.setUnitPrice(unitPrice);
            restoreDriverDeduction.setMemo(dto.getMemo());
            restoreDriverDeduction.setStatus(DriverDeductionStatus.ACTIVE);
            DriverDeduction savedDriverDeduction = driverDeductionRepository.save(restoreDriverDeduction);

            deleteLogService.removeIfExists(TableRef.DRIVER_DEDUCTION, savedDriverDeduction.getId());

            int totalAllowance = driverAllowanceRepository.sumAmountByDriverPayrollId(payrollId);
            int totalDeduction = driverDeductionRepository.sumAmountByDriverPayrollId(payrollId);

            payroll.applyTotals(totalAllowance, totalDeduction);

            data = toCreateDriverDeductionResponseDto(savedDriverDeduction);

            return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
        }

        DriverDeduction newDriverDeduction = DriverDeduction.builder()
                .driverPayroll(payroll)
                .deductionType(deductionType)
                .quantity(dto.getQuantity())
                .unitPrice(unitPrice)
                .memo(dto.getMemo())
                .status(DriverDeductionStatus.ACTIVE)
                .build();

        DriverDeduction savedDriverDeduction = driverDeductionRepository.save(newDriverDeduction);

        int totalAllowance = driverAllowanceRepository.sumAmountByDriverPayrollId(payrollId);
        int totalDeduction = driverDeductionRepository.sumAmountByDriverPayrollId(payrollId);

        payroll.applyTotals(totalAllowance, totalDeduction);

        data = toCreateDriverDeductionResponseDto(savedDriverDeduction);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<List<GetDriverDeductionDetailResponseDto>> getDriverDeduction(Long payrollId) {
        List<GetDriverDeductionDetailResponseDto> data = null;

        List<DriverDeduction> driverDeductions = driverDeductionRepository.findByDriverPayrollIdAndStatus(payrollId, DriverDeductionStatus.ACTIVE);

        if (driverDeductions.isEmpty()) {
            throw new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND);
        }

        data = driverDeductions.stream()
                .map(this::toGetDriverDeductionDetailResponseDto)
                .collect(Collectors.toList());

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    @Transactional
    public ResponseDto<List<UpdateDriverDeductionResponseDto>> updateDriverDeduction(UserPrincipal userPrincipal, Long payrollId, List<UpdateDriverDeductionRequestDto.Item> items) {
        List<UpdateDriverDeductionResponseDto> data = new ArrayList<>(items.size());

        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        DriverPayroll payroll = driverPayrollService.getDriverPayrollForUpdate(payrollId);

        if (payroll.getStatus() == DriverPayrollStatus.CONFIRMED) {
            return ResponseDto.fail(ResponseCode.LOCK_PAYROLL_CONFIRMED, ResponseMessage.LOCK_PAYROLL_CONFIRMED);
        } else if (payroll.getStatus() == DriverPayrollStatus.DELETED) {
            return ResponseDto.fail(ResponseCode.ALREADY_DELETED,ResponseMessage.ALREADY_DELETED);
        }

        Set<Long> ids = items.stream()
                .map(UpdateDriverDeductionRequestDto.Item::getId)
                .collect(Collectors.toSet());
        List<DriverDeduction> deductions = driverDeductionRepository.findAllByIdInAndDriverPayrollId(ids, payrollId);
        Map<Long, DriverDeduction> byId = deductions.stream().collect(Collectors.toMap(DriverDeduction::getId, a -> a));

        if (byId.size() != ids.size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.RESOURCE_NOT_FOUND);
        }

        for (UpdateDriverDeductionRequestDto.Item item : items) {
            DriverDeduction driverDeduction = byId.get(item.getId());

            if (item.getQuantity() != null && item.getQuantity().compareTo(driverDeduction.getQuantity()) != 0) {
                String prevData = driverDeduction.getQuantity().toString();
                driverDeduction.setQuantity(item.getQuantity());
                createUpdateLog(user, driverDeduction, "quantity", prevData, item.getQuantity().toString());
            }

            if (item.getUnitPrice() != null && !Objects.equals(item.getUnitPrice(), driverDeduction.getUnitPrice())) {
                String prevData = String.valueOf(driverDeduction.getUnitPrice());
                driverDeduction.setUnitPrice(item.getUnitPrice());
                createUpdateLog(user, driverDeduction, "unit_price", prevData, String.valueOf(item.getUnitPrice()));
            }

            if (item.getMemo() != null && !Objects.equals(item.getMemo(), driverDeduction.getMemo())) {
                String prevData = driverDeduction.getMemo();
                driverDeduction.setMemo(item.getMemo());
                createUpdateLog(user, driverDeduction, "memo", prevData, item.getMemo());
            }

            driverDeduction.calculateAmount();

            DriverDeduction updatedDriverDeduction = driverDeductionRepository.save(driverDeduction);

            data.add(toUpdateDriverDeductionResponseDto(updatedDriverDeduction));
        }

        int totalAllowance = driverAllowanceRepository.sumAmountByDriverPayrollId(payrollId);
        int totalDeduction = driverDeductionRepository.sumAmountByDriverPayrollId(payrollId);

        payroll.applyTotals(totalAllowance, totalDeduction);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    @Transactional
    public ResponseDto<Void> deleteDriverDeduction(UserPrincipal userPrincipal, Long payrollId, Long deductionId) {
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        DriverPayroll payroll = driverPayrollService.getDriverPayrollForUpdate(payrollId);

        if (payroll.getStatus() == DriverPayrollStatus.CONFIRMED) {
            return ResponseDto.fail(ResponseCode.LOCK_PAYROLL_CONFIRMED, ResponseMessage.LOCK_PAYROLL_CONFIRMED);
        } else if (payroll.getStatus() == DriverPayrollStatus.DELETED) {
            return ResponseDto.fail(ResponseCode.ALREADY_DELETED,ResponseMessage.ALREADY_DELETED);
        }

        DriverDeduction driverDeduction = driverDeductionRepository.findById(deductionId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        if (driverDeduction.getStatus() == DriverDeductionStatus.DELETED) {
            return ResponseDto.fail(ResponseCode.ALREADY_DELETED,ResponseMessage.ALREADY_DELETED);
        }

        driverDeduction.setStatus(DriverDeductionStatus.DELETED);
        driverDeductionRepository.save(driverDeduction);

        int totalAllowance = driverAllowanceRepository.sumAmountByDriverPayrollId(payrollId);
        int totalDeduction = driverDeductionRepository.sumAmountByDriverPayrollId(payrollId);

        payroll.applyTotals(totalAllowance, totalDeduction);

        deleteLogService.createLog(TableRef.DRIVER_DEDUCTION, deductionId, user);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    private CreateDriverDeductionResponseDto toCreateDriverDeductionResponseDto(DriverDeduction driverDeduction) {
        return CreateDriverDeductionResponseDto.builder()
                .id(driverDeduction.getId())
                .deductionTypeCode(driverDeduction.getDeductionType().getCode())
                .deductionTypeName(driverDeduction.getDeductionType().getName())
                .quantity(driverDeduction.getQuantity())
                .unitPrice(driverDeduction.getUnitPrice())
                .amount(driverDeduction.getAmount())
                .memo(driverDeduction.getMemo())
                .status(driverDeduction.getStatus())
                .createdAt(DateUtils.format(driverDeduction.getCreatedAt()))
                .updatedAt(DateUtils.format(driverDeduction.getUpdatedAt()))
                .build();
    }

    private GetDriverDeductionDetailResponseDto toGetDriverDeductionDetailResponseDto(DriverDeduction driverDeduction) {
        return GetDriverDeductionDetailResponseDto.builder()
                .id(driverDeduction.getId())
                .deductionTypeId(driverDeduction.getDeductionType().getId())
                .deductionTypeCode(driverDeduction.getDeductionType().getCode())
                .deductionTypeName(driverDeduction.getDeductionType().getName())
                .quantity(driverDeduction.getQuantity())
                .unitPrice(driverDeduction.getUnitPrice())
                .amount(driverDeduction.getAmount())
                .memo(driverDeduction.getMemo())
                .createdAt(DateUtils.format(driverDeduction.getCreatedAt()))
                .updatedAt(DateUtils.format(driverDeduction.getUpdatedAt()))
                .build();
    }

    private UpdateDriverDeductionResponseDto toUpdateDriverDeductionResponseDto(DriverDeduction driverDeduction) {
        return UpdateDriverDeductionResponseDto.builder()
                .id(driverDeduction.getId())
                .deductionTypeCode(driverDeduction.getDeductionType().getCode())
                .deductionTypeName(driverDeduction.getDeductionType().getName())
                .quantity(driverDeduction.getQuantity())
                .unitPrice(driverDeduction.getUnitPrice())
                .amount(driverDeduction.getAmount())
                .memo(driverDeduction.getMemo())
                .createdAt(DateUtils.format(driverDeduction.getCreatedAt()))
                .updatedAt(DateUtils.format(driverDeduction.getUpdatedAt()))
                .build();
    }

    private void createUpdateLog(User user, DriverDeduction updatedDriverDeduction, String type, String prevData, String newData) {
        DriverDeductionUpdateLog driverDeductionUpdateLog = DriverDeductionUpdateLog.builder()
                .driverDeduction(updatedDriverDeduction)
                .user(user)
                .changedByUsername(user.getUsername())
                .type(type)
                .prevData(prevData)
                .newData(newData)
                .build();

        driverDeductionUpdateLogRepository.save(driverDeductionUpdateLog);
    }
}

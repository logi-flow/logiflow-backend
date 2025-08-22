package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.constants.ResponseCode;
import com.logi_flow.backend.common.constants.ResponseMessage;
import com.logi_flow.backend.common.enums.DriverPayrollStatus;
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
    private final DriverDeductionUpdateLogRepository driverDeductionUpdateLogRepository;
    private final DriverPayrollService driverPayrollService;
    private final DeductionTypeService deductionTypeService;

    @Override
    @Transactional
    public ResponseDto<CreateDriverDeductionResponseDto> createDriverDeduction(Long payrollId, CreateDriverDeductionRequestDto dto) {
        CreateDriverDeductionResponseDto data = null;
        int unitPrice = dto.getUnitPrice() == null ? 0 : dto.getUnitPrice();

        DriverPayroll payroll = driverPayrollService.getDriverPayroll(payrollId);
        DeductionType deductionType = deductionTypeService.getDeductionType(dto.getDeductionTypeId());

        DriverDeduction newDriverDeduction = DriverDeduction.builder()
                .driverPayroll(payroll)
                .deductionType(deductionType)
                .quantity(dto.getQuantity())
                .unitPrice(unitPrice)
                .memo(dto.getMemo())
                .build();

        DriverDeduction savedDriverDeduction = driverDeductionRepository.save(newDriverDeduction);

        data = CreateDriverDeductionResponseDto.builder()
                .id(savedDriverDeduction.getId())
                .deductionTypeCode(savedDriverDeduction.getDeductionType().getCode())
                .deductionTypeName(savedDriverDeduction.getDeductionType().getName())
                .quantity(savedDriverDeduction.getQuantity())
                .unitPrice(savedDriverDeduction.getUnitPrice())
                .amount(savedDriverDeduction.getAmount())
                .memo(savedDriverDeduction.getMemo())
                .createdAt(DateUtils.format(savedDriverDeduction.getCreatedAt()))
                .updatedAt(DateUtils.format(savedDriverDeduction.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<GetDriverDeductionDetailResponseDto> getDriverDeduction(Long payrollId) {
        GetDriverDeductionDetailResponseDto data = null;

        DriverDeduction driverDeduction = driverDeductionRepository.findByDriverPayrollId(payrollId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        data = GetDriverDeductionDetailResponseDto.builder()
                .deductionTypeCode(driverDeduction.getDeductionType().getCode())
                .deductionTypeName(driverDeduction.getDeductionType().getName())
                .quantity(driverDeduction.getQuantity())
                .unitPrice(driverDeduction.getUnitPrice())
                .amount(driverDeduction.getAmount())
                .memo(driverDeduction.getMemo())
                .createdAt(DateUtils.format(driverDeduction.getCreatedAt()))
                .updatedAt(DateUtils.format(driverDeduction.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    @Transactional
    public ResponseDto<List<UpdateDriverDeductionResponseDto>> updateDriverDeduction(UserPrincipal userPrincipal, Long payrollId, List<UpdateDriverDeductionRequestDto.Item> items) {
        List<UpdateDriverDeductionResponseDto> data = new ArrayList<>(items.size());

        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        DriverPayroll payroll = driverPayrollService.getDriverPayroll(payrollId);

        if (!payroll.getStatus().equals(DriverPayrollStatus.CREATED)) {
            return ResponseDto.fail(ResponseCode.LOCK_PAYROLL_CONFIRMED, ResponseMessage.LOCK_PAYROLL_CONFIRMED);
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

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
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

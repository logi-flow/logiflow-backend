package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.constants.ResponseCode;
import com.logi_flow.backend.common.constants.ResponseMessage;
import com.logi_flow.backend.common.enums.DriverPayrollStatus;
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
import com.logi_flow.backend.repository.UserRepository;
import com.logi_flow.backend.service.AllowanceTypeService;
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
    private final DriverAllowanceUpdateLogRepository driverAllowanceUpdateLogRepository;
    private final DriverPayrollService driverPayrollService;
    private final AllowanceTypeService allowanceTypeService;

    @Override
    @Transactional
    public ResponseDto<CreateDriverAllowanceResponseDto> createDriverAllowance(Long payrollId, CreateDriverAllowanceRequestDto dto) {
        CreateDriverAllowanceResponseDto data = null;
        int unitPrice = dto.getUnitPrice() == null ? 0 : dto.getUnitPrice();

        DriverPayroll payroll = driverPayrollService.getDriverPayroll(payrollId);
        AllowanceType allowanceType = allowanceTypeService.getAllowanceType(dto.getAllowanceTypeId());

        DriverAllowance newDriverAllowance = DriverAllowance.builder()
                .driverPayroll(payroll)
                .allowanceType(allowanceType)
                .quantity(dto.getQuantity())
                .unitPrice(unitPrice)
                .memo(dto.getMemo())
                .build();

        DriverAllowance savedDriverAllowance = driverAllowanceRepository.save(newDriverAllowance);

        data = CreateDriverAllowanceResponseDto.builder()
                .id(savedDriverAllowance.getId())
                .allowanceTypeCode(savedDriverAllowance.getAllowanceType().getCode())
                .allowanceTypeName(savedDriverAllowance.getAllowanceType().getName())
                .quantity(savedDriverAllowance.getQuantity())
                .unitPrice(savedDriverAllowance.getUnitPrice())
                .amount(savedDriverAllowance.getAmount())
                .memo(savedDriverAllowance.getMemo())
                .createdAt(DateUtils.format(savedDriverAllowance.getCreatedAt()))
                .updatedAt(DateUtils.format(savedDriverAllowance.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<GetDriverAllowanceDetailResponseDto> getDriverAllowance(Long payrollId) {
        GetDriverAllowanceDetailResponseDto data = null;

        DriverAllowance driverAllowance = driverAllowanceRepository.findByDriverPayrollId(payrollId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        data = GetDriverAllowanceDetailResponseDto.builder()
                .allowanceTypeCode(driverAllowance.getAllowanceType().getCode())
                .allowanceTypeName(driverAllowance.getAllowanceType().getName())
                .quantity(driverAllowance.getQuantity())
                .unitPrice(driverAllowance.getUnitPrice())
                .amount(driverAllowance.getAmount())
                .memo(driverAllowance.getMemo())
                .createdAt(DateUtils.format(driverAllowance.getCreatedAt()))
                .updatedAt(DateUtils.format(driverAllowance.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    @Transactional
    public ResponseDto<List<UpdateDriverAllowanceResponseDto>> updateDriverAllowance(UserPrincipal userPrincipal, Long payrollId, List<UpdateDriverAllowanceRequestDto.Item> items) {
        List<UpdateDriverAllowanceResponseDto> data = new ArrayList<>(items.size());

        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        DriverPayroll payroll = driverPayrollService.getDriverPayroll(payrollId);

        if (!payroll.getStatus().equals(DriverPayrollStatus.CREATED)) {
            return ResponseDto.fail(ResponseCode.LOCK_PAYROLL_CONFIRMED, ResponseMessage.LOCK_PAYROLL_CONFIRMED);
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

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
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

package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.constants.ResponseCode;
import com.logi_flow.backend.common.constants.ResponseMessage;
import com.logi_flow.backend.common.util.DateUtils;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.driverAllowance.request.CreateDriverAllowanceRequestDto;
import com.logi_flow.backend.dto.driverAllowance.request.UpdateDriverAllowanceRequestDto;
import com.logi_flow.backend.dto.driverAllowance.response.CreateDriverAllowanceResponseDto;
import com.logi_flow.backend.dto.driverAllowance.response.GetDriverAllowanceDetailResponseDto;
import com.logi_flow.backend.dto.driverAllowance.response.UpdateDriverAllowanceResponseDto;
import com.logi_flow.backend.entity.AllowanceType;
import com.logi_flow.backend.entity.DriverAllowance;
import com.logi_flow.backend.entity.DriverPayroll;
import com.logi_flow.backend.repository.DriverAllowanceRepository;
import com.logi_flow.backend.service.AllowanceTypeService;
import com.logi_flow.backend.service.DriverAllowanceService;
import com.logi_flow.backend.service.DriverPayrollService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DriverAllowanceServiceImpl implements DriverAllowanceService {

    private final DriverAllowanceRepository driverAllowanceRepository;
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
        return null;
    }

    @Override
    public ResponseDto<UpdateDriverAllowanceResponseDto> updateDriverAllowance(UserPrincipal userPrincipal, Long payrollId, Long allowanceId, UpdateDriverAllowanceRequestDto dto) {
        return null;
    }

    @Override
    public ResponseDto<Void> deleteDriverAllowance(UserPrincipal userPrincipal, Long payrollId, Long allowanceId) {
        return null;
    }
}

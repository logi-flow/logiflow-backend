package com.logi_flow.backend.common.mapper;

import com.logi_flow.backend.dto.driverPayroll.response.AllowanceItemDto;
import com.logi_flow.backend.dto.driverPayroll.response.DeductionItemDto;
import com.logi_flow.backend.dto.driverPayroll.response.GetDriverPayrollDetailResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DriverPayrollMapper {
    GetDriverPayrollDetailResponseDto getDriverPayrollDetail(@Param("payrollId") Long payrollId);
    List<AllowanceItemDto> selectAllowanceItemsByDriverPayrollId(@Param("payrollId") Long payrollId);
    List<DeductionItemDto> selectDeductionItemsByDriverPayrollId(@Param("payrollId") Long payrollId);
}

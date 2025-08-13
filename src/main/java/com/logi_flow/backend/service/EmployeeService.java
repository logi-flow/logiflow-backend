package com.logi_flow.backend.service;

import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.employee.request.CreateEmployeeRequestDto;
import com.logi_flow.backend.dto.employee.request.UpdateEmployeeAdminRequestDto;
import com.logi_flow.backend.dto.employee.request.UpdateEmployeeRequestDto;
import com.logi_flow.backend.dto.employee.response.CreateEmployeeResponseDto;
import com.logi_flow.backend.dto.employee.response.GetAllEmployeeResponseDto;
import com.logi_flow.backend.dto.employee.response.GetEmployeeDetailResponseDto;
import com.logi_flow.backend.dto.employee.response.UpdateEmployeeResponseDto;
import jakarta.validation.Valid;

public interface EmployeeService {
    ResponseDto<UpdateEmployeeResponseDto> updateEmployee(Long id, @Valid UpdateEmployeeRequestDto dto);

    ResponseDto<GetEmployeeDetailResponseDto> getEmployeeDetail(Long id);

    ResponseDto<CreateEmployeeResponseDto> createEmployee(Long id, Long employeeId, @Valid CreateEmployeeRequestDto dto);

    ResponseDto<UpdateEmployeeResponseDto> updateEmployeeAdmin(Long id, Long employeeId, @Valid UpdateEmployeeAdminRequestDto dto);

    ResponseDto<GetAllEmployeeResponseDto> getAllEmployee(Long id);

    ResponseDto<GetEmployeeDetailResponseDto> getEmployeeDetailAdmin(Long id, Long employeeId);

    ResponseDto<?> deleteEmployee(Long id, Long employeeId);
}

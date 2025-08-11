package com.logi_flow.backend.service;

import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.employee.request.CreateEmployeeRequestDto;
import com.logi_flow.backend.dto.employee.request.UpdateEmployeeRequestDto;
import com.logi_flow.backend.dto.employee.response.CreateEmployeeResponseDto;
import com.logi_flow.backend.dto.employee.response.GetAllEmployeeResponseDto;
import com.logi_flow.backend.dto.employee.response.GetEmployeeDetailResponseDto;
import com.logi_flow.backend.dto.employee.response.UpdateEmployeeResponseDto;
import jakarta.validation.Valid;

public interface EmployeeService {
    ResponseDto<CreateEmployeeResponseDto> createEmployee(Long id, Long employeeId, @Valid CreateEmployeeRequestDto dto);

    ResponseDto<UpdateEmployeeResponseDto> updateEmployee(Long id, Long employeeId, @Valid UpdateEmployeeRequestDto dto);

    ResponseDto<GetAllEmployeeResponseDto> getAllEmployee(Long id);

    ResponseDto<GetEmployeeDetailResponseDto> getEmployeeDetail(Long id, Long employeeId);

    ResponseDto<?> deleteEmployee(Long id, Long employeeId);
}

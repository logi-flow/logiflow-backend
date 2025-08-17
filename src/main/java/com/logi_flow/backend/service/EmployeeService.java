package com.logi_flow.backend.service;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.employee.request.CreateEmployeeRequestDto;
import com.logi_flow.backend.dto.employee.request.UpdateEmployeeAdminRequestDto;
import com.logi_flow.backend.dto.employee.request.UpdateEmployeeRequestDto;
import com.logi_flow.backend.dto.employee.response.CreateEmployeeResponseDto;
import com.logi_flow.backend.dto.employee.response.GetAllEmployeeResponseDto;
import com.logi_flow.backend.dto.employee.response.GetEmployeeDetailResponseDto;
import com.logi_flow.backend.dto.employee.response.UpdateEmployeeResponseDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

public interface EmployeeService {
    ResponseDto<UpdateEmployeeResponseDto> updateEmployee(UserPrincipal userPrincipal, @Valid UpdateEmployeeRequestDto dto);

    ResponseDto<GetEmployeeDetailResponseDto> getEmployeeDetail(UserPrincipal userPrincipal);

    ResponseDto<CreateEmployeeResponseDto> createEmployee(UserPrincipal userPrincipal, Long employeeId, @Valid CreateEmployeeRequestDto dto);

    ResponseDto<UpdateEmployeeResponseDto> updateEmployeeAdmin(UserPrincipal userPrincipal, Long employeeId, @Valid UpdateEmployeeAdminRequestDto dto);

    Page<GetAllEmployeeResponseDto> getAllEmployee(UserPrincipal userPrincipal, int page, int size, String sort);

    ResponseDto<GetEmployeeDetailResponseDto> getEmployeeDetailAdmin(UserPrincipal userPrincipal, Long employeeId);

    ResponseDto<?> deleteEmployee(UserPrincipal userPrincipal, Long employeeId);
}

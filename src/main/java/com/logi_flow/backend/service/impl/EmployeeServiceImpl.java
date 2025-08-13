package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.employee.request.CreateEmployeeRequestDto;
import com.logi_flow.backend.dto.employee.request.UpdateEmployeeAdminRequestDto;
import com.logi_flow.backend.dto.employee.request.UpdateEmployeeRequestDto;
import com.logi_flow.backend.dto.employee.response.CreateEmployeeResponseDto;
import com.logi_flow.backend.dto.employee.response.GetAllEmployeeResponseDto;
import com.logi_flow.backend.dto.employee.response.GetEmployeeDetailResponseDto;
import com.logi_flow.backend.dto.employee.response.UpdateEmployeeResponseDto;
import com.logi_flow.backend.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Override
    public ResponseDto<UpdateEmployeeResponseDto> updateEmployee(UserPrincipal userPrincipal, UpdateEmployeeRequestDto dto) {
        return null;
    }

    @Override
    public ResponseDto<GetEmployeeDetailResponseDto> getEmployeeDetail(UserPrincipal userPrincipal) {
        return null;
    }

    @Override
    public ResponseDto<CreateEmployeeResponseDto> createEmployee(UserPrincipal userPrincipal, Long employeeId, CreateEmployeeRequestDto dto) {
        return null;
    }

    @Override
    public ResponseDto<UpdateEmployeeResponseDto> updateEmployeeAdmin(UserPrincipal userPrincipal, Long employeeId, UpdateEmployeeAdminRequestDto dto) {
        return null;
    }

    @Override
    public Page<GetAllEmployeeResponseDto> getAllEmployee(UserPrincipal userPrincipal, int page, int size, String sort) {
        return null;
    }

    @Override
    public ResponseDto<GetEmployeeDetailResponseDto> getEmployeeDetailAdmin(UserPrincipal userPrincipal, Long employeeId) {
        return null;
    }

    @Override
    public ResponseDto<?> deleteEmployee(UserPrincipal userPrincipal, Long employeeId) {
        return null;
    }
}

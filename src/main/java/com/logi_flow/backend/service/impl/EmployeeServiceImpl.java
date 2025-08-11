package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.employee.request.CreateEmployeeRequestDto;
import com.logi_flow.backend.dto.employee.request.UpdateEmployeeRequestDto;
import com.logi_flow.backend.dto.employee.response.CreateEmployeeResponseDto;
import com.logi_flow.backend.dto.employee.response.GetAllEmployeeResponseDto;
import com.logi_flow.backend.dto.employee.response.GetEmployeeDetailResponseDto;
import com.logi_flow.backend.dto.employee.response.UpdateEmployeeResponseDto;
import com.logi_flow.backend.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Override
    public ResponseDto<CreateEmployeeResponseDto> createEmployee(Long id, Long employeeId, CreateEmployeeRequestDto dto) {
        return null;
    }

    @Override
    public ResponseDto<UpdateEmployeeResponseDto> updateEmployee(Long id, Long employeeId, UpdateEmployeeRequestDto dto) {
        return null;
    }

    @Override
    public ResponseDto<GetAllEmployeeResponseDto> getAllEmployee(Long id) {
        return null;
    }

    @Override
    public ResponseDto<GetEmployeeDetailResponseDto> getEmployeeDetail(Long id, Long employeeId) {
        return null;
    }

    @Override
    public ResponseDto<?> deleteEmployee(Long id, Long employeeId) {
        return null;
    }
}

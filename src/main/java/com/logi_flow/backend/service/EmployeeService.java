package com.logi_flow.backend.service;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.employee.request.CreateEmployeeRequestDto;
import com.logi_flow.backend.dto.employee.request.UpdateEmployeeAdminRequestDto;
import com.logi_flow.backend.dto.employee.request.UpdateEmployeeRequestDto;
import com.logi_flow.backend.dto.employee.request.UpdateEmployeeStatusRequestDto;
import com.logi_flow.backend.dto.employee.response.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface EmployeeService {
    ResponseDto<CreateEmployeeResponseDto> createEmployee(UserPrincipal userPrincipal, @Valid CreateEmployeeRequestDto dto, MultipartFile profileImage);

    ResponseDto<UpdateEmployeeResponseDto> updateEmployee(UserPrincipal userPrincipal, @Valid UpdateEmployeeRequestDto dto);

    ResponseDto<GetEmployeeDetailResponseDto> getEmployeeDetail(UserPrincipal userPrincipal);

    ResponseDto<UpdateEmployeeResponseDto> updateEmployeeAdmin(UserPrincipal userPrincipal, Long employeeId, @Valid UpdateEmployeeAdminRequestDto dto, MultipartFile profileImage);

    ResponseDto<UpdateEmployeeStatusResponseDto> updateEmployeeStatus(UserPrincipal userPrincipal, Long employeeId, @Valid UpdateEmployeeStatusRequestDto dto);

    Page<GetAllEmployeeResponseDto> getAllEmployee(UserPrincipal userPrincipal, int page, int size, String sort);

    ResponseDto<GetEmployeeDetailAdminResponseDto> getEmployeeDetailAdmin(UserPrincipal userPrincipal, Long employeeId);
}

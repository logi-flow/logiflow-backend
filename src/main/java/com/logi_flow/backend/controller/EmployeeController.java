package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.common.mapper.PageMapper;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.PageDto;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.customer.request.UpdateCustomerStatusRequestDto;
import com.logi_flow.backend.dto.customer.response.UpdateCustomerStatusResponseDto;
import com.logi_flow.backend.dto.employee.request.CreateEmployeeRequestDto;
import com.logi_flow.backend.dto.employee.request.UpdateEmployeeAdminRequestDto;
import com.logi_flow.backend.dto.employee.request.UpdateEmployeeRequestDto;
import com.logi_flow.backend.dto.employee.request.UpdateEmployeeStatusRequestDto;
import com.logi_flow.backend.dto.employee.response.*;
import com.logi_flow.backend.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.EMPLOYEE_API)
public class EmployeeController {

    private final EmployeeService employeeService;

    private static final String EMPLOYEE_MY_INFO_API = "/me";
    private static final String EMPLOYEE_ID_API = "/{employeeId}";
    private static final String EMPLOYEE_STATUS_API = "/{employeeId}/status";

    @PostMapping
    public ResponseEntity<ResponseDto<CreateEmployeeResponseDto>> createEmployee(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody CreateEmployeeRequestDto dto
    ){
        ResponseDto<CreateEmployeeResponseDto> response = employeeService.createEmployee(userPrincipal, dto);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    @PutMapping(EMPLOYEE_MY_INFO_API)
    public ResponseEntity<ResponseDto<UpdateEmployeeResponseDto>> updateEmployee(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody UpdateEmployeeRequestDto dto
    ){
        ResponseDto<UpdateEmployeeResponseDto> response = employeeService.updateEmployee(userPrincipal, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping(EMPLOYEE_MY_INFO_API)
    public ResponseEntity<ResponseDto<GetEmployeeDetailResponseDto>> getEmployeeDetail(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        ResponseDto<GetEmployeeDetailResponseDto> response = employeeService.getEmployeeDetail(userPrincipal);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @PutMapping(EMPLOYEE_ID_API)
    public ResponseEntity<ResponseDto<UpdateEmployeeResponseDto>> updateEmployeeAdmin(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long employeeId,
            @Valid @RequestBody UpdateEmployeeAdminRequestDto dto
    ){
        ResponseDto<UpdateEmployeeResponseDto> response = employeeService.updateEmployeeAdmin(userPrincipal, employeeId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @PutMapping(EMPLOYEE_STATUS_API)
    public ResponseEntity<ResponseDto<UpdateEmployeeStatusResponseDto>> updateEmployeeStatus(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long employeeId,
            @Valid @RequestBody UpdateEmployeeStatusRequestDto dto
    ){
        ResponseDto<UpdateEmployeeStatusResponseDto> response = employeeService.updateEmployeeStatus(userPrincipal, employeeId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }


    @GetMapping
    public ResponseEntity<ResponseDto<PageDto<GetAllEmployeeResponseDto>>> getAllEmployee(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetAllEmployeeResponseDto> result = employeeService.getAllEmployee(userPrincipal, page, size, sort);
        PageDto<GetAllEmployeeResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping(EMPLOYEE_ID_API)
    public ResponseEntity<ResponseDto<GetEmployeeDetailAdminResponseDto>> getEmployeeDetailAdmin(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long employeeId
    ) {
        ResponseDto<GetEmployeeDetailAdminResponseDto> response = employeeService.getEmployeeDetailAdmin(userPrincipal, employeeId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @DeleteMapping(EMPLOYEE_ID_API)
    public ResponseEntity<ResponseDto<?>> deleteEmployee(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long employeeId
    ) {
        ResponseDto<?> response = employeeService.deleteEmployee(userPrincipal, employeeId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

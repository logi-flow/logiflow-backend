package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.employee.request.CreateEmployeeRequestDto;
import com.logi_flow.backend.dto.employee.request.UpdateEmployeeRequestDto;
import com.logi_flow.backend.dto.employee.response.CreateEmployeeResponseDto;
import com.logi_flow.backend.dto.employee.response.GetAllEmployeeResponseDto;
import com.logi_flow.backend.dto.employee.response.GetEmployeeDetailResponseDto;
import com.logi_flow.backend.dto.employee.response.UpdateEmployeeResponseDto;
import com.logi_flow.backend.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.EMPLOYEE_API)
public class EmployeeController {

    private final EmployeeService employeeService;

    private static final String EMPLOYEE_ID_API = "/{employeeId}";

    @PostMapping(EMPLOYEE_ID_API)
    public ResponseEntity<ResponseDto<CreateEmployeeResponseDto>> createEmployee(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long employeeId,
            @Valid @RequestBody CreateEmployeeRequestDto dto
    ){
        Long id = userPrincipal.getId();
        ResponseDto<CreateEmployeeResponseDto> response = employeeService.createEmployee(id, employeeId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    @PutMapping(EMPLOYEE_ID_API)
    public ResponseEntity<ResponseDto<UpdateEmployeeResponseDto>> updateEmployee(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long employeeId,
            @Valid @RequestBody UpdateEmployeeRequestDto dto
    ){
        Long id = userPrincipal.getId();
        ResponseDto<UpdateEmployeeResponseDto> response = employeeService.updateEmployee(id, employeeId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<GetAllEmployeeResponseDto>> getAllEmployee(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        Long id = userPrincipal.getId();
        ResponseDto<GetAllEmployeeResponseDto> response = employeeService.getAllEmployee(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(EMPLOYEE_ID_API)
    public ResponseEntity<ResponseDto<GetEmployeeDetailResponseDto>> getEmployeeDetail(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long employeeId
    ) {
        Long id = userPrincipal.getId();
        ResponseDto<GetEmployeeDetailResponseDto> response = employeeService.getEmployeeDetail(id, employeeId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(EMPLOYEE_ID_API)
    public ResponseEntity<ResponseDto<?>> deleteEmployee(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long employeeId
    ) {
        Long id = userPrincipal.getId();
        ResponseDto<?> response = employeeService.deleteEmployee(id, employeeId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

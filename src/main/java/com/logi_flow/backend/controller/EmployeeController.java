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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "직원 관리", description = "직원(Employee) 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.EMPLOYEE_API)
public class EmployeeController {

    private final EmployeeService employeeService;

    private static final String EMPLOYEE_MY_INFO_API = "/me";
    private static final String EMPLOYEE_ID_API = "/{employeeId}";
    private static final String EMPLOYEE_STATUS_API = "/{employeeId}/status";

    @Operation(summary = "신규 직원 생성", description = "직원 정보를 입력하여 ID / PW를 생성함")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HUMAN_RESOURCES_MANAGER')")
    public ResponseEntity<ResponseDto<CreateEmployeeResponseDto>> createEmployee(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestPart(value = "dto") CreateEmployeeRequestDto dto,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage
    ){
        ResponseDto<CreateEmployeeResponseDto> response = employeeService.createEmployee(userPrincipal, dto, profileImage);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    @Operation(summary = "직원 내 정보 수정", description = "직원 본인의 정보를 수정")
    @PutMapping(EMPLOYEE_MY_INFO_API)
    @PreAuthorize("hasAnyRole('EMPLOYEE')")
    public ResponseEntity<ResponseDto<UpdateEmployeeResponseDto>> updateEmployee(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody UpdateEmployeeRequestDto dto
    ){
        ResponseDto<UpdateEmployeeResponseDto> response = employeeService.updateEmployee(userPrincipal, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "직원 내 정보 조회", description = "직원 본인의 정보 조회")
    @GetMapping(EMPLOYEE_MY_INFO_API)
    @PreAuthorize("hasAnyRole('EMPLOYEE')")
    public ResponseEntity<ResponseDto<GetEmployeeDetailResponseDto>> getEmployeeDetail(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        ResponseDto<GetEmployeeDetailResponseDto> response = employeeService.getEmployeeDetail(userPrincipal);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "직원 정보 수정", description = "관리자, 담당자에 의한 직원 정보 수정")
    @PutMapping(EMPLOYEE_ID_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'HUMAN_RESOURCES_MANAGER')")
    public ResponseEntity<ResponseDto<UpdateEmployeeResponseDto>> updateEmployeeAdmin(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long employeeId,
            @Valid @RequestPart(value = "dto") UpdateEmployeeAdminRequestDto dto,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage
    ){
        ResponseDto<UpdateEmployeeResponseDto> response = employeeService.updateEmployeeAdmin(userPrincipal, employeeId, dto, profileImage);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "직원 상태 수정", description = "직원의 상태를 수정")
    @PutMapping(EMPLOYEE_STATUS_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'HUMAN_RESOURCES_MANAGER')")
    public ResponseEntity<ResponseDto<UpdateEmployeeStatusResponseDto>> updateEmployeeStatus(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long employeeId,
            @Valid @RequestBody UpdateEmployeeStatusRequestDto dto
    ){
        ResponseDto<UpdateEmployeeStatusResponseDto> response = employeeService.updateEmployeeStatus(userPrincipal, employeeId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "모든 직원 조회", description = "직원을 모두 조회")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HUMAN_RESOURCES_MANAGER')")
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

    @Operation(summary = "직원 세부정보 조회", description = "관리자, 담당자의 직원 세부정보 조회")
    @GetMapping(EMPLOYEE_ID_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'HUMAN_RESOURCES_MANAGER')")
    public ResponseEntity<ResponseDto<GetEmployeeDetailAdminResponseDto>> getEmployeeDetailAdmin(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long employeeId
    ) {
        ResponseDto<GetEmployeeDetailAdminResponseDto> response = employeeService.getEmployeeDetailAdmin(userPrincipal, employeeId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }
}

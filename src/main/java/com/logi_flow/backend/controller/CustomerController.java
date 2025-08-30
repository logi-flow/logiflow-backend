package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.common.mapper.PageMapper;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.PageDto;
import com.logi_flow.backend.dto.ResponseDto;

import com.logi_flow.backend.dto.customer.request.UpdateCustomerAdminRequestDto;
import com.logi_flow.backend.dto.customer.request.UpdateCustomerRequestDto;
import com.logi_flow.backend.dto.customer.request.UpdateCustomerStatusRequestDto;
import com.logi_flow.backend.dto.customer.response.*;
import com.logi_flow.backend.service.CustomerService;
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

@Tag(name = "고객사 관리", description = "고객사(Customer) 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.CUSTOMER_API)
public class CustomerController {

    private final CustomerService customerService;

    private static final String CUSTOMER_MY_INFO_API = "/me";
    private static final String CUSTOMER_ID_API = "/{customerId}";
    private static final String CUSTOMER_STATUS_API = "/{customerId}/status";

    @Operation(summary = "고객사 내 정보 수정", description = "고객사 본인의 정보를 수정")
    @PutMapping(CUSTOMER_MY_INFO_API)
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<ResponseDto<UpdateCustomerResponseDto>> updateCustomer(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody UpdateCustomerRequestDto dto
    ){
        ResponseDto<UpdateCustomerResponseDto> response = customerService.updateCustomer(userPrincipal, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "고객사 내 정보 조회", description = "고객사 본인의 정보 조회")
    @GetMapping(CUSTOMER_MY_INFO_API)
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<ResponseDto<GetCustomerDetailResponseDto>> getCustomerDetail(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        ResponseDto<GetCustomerDetailResponseDto> response = customerService.getCustomerDetail(userPrincipal);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "고객사 정보 수정", description = "관리자, 담당자에 의한 고객사 정보 수정")
    @PutMapping(CUSTOMER_ID_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'CONTRACTS_MANAGER')")
    public ResponseEntity<ResponseDto<UpdateCustomerResponseDto>> updateCustomerAdmin(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long customerId,
            @Valid @RequestBody UpdateCustomerAdminRequestDto dto
    ){
        ResponseDto<UpdateCustomerResponseDto> response = customerService.updateCustomerAdmin(userPrincipal, customerId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "고객사 상태 수정", description = "고객사의 상태를 수정")
    @PutMapping(CUSTOMER_STATUS_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'CONTRACTS_MANAGER')")
    public ResponseEntity<ResponseDto<UpdateCustomerStatusResponseDto>> updateCustomerStatus(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long customerId,
            @Valid @RequestBody UpdateCustomerStatusRequestDto dto
    ){
        ResponseDto<UpdateCustomerStatusResponseDto> response = customerService.updateCustomerStatus(userPrincipal, customerId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "모든 고객사 조회", description = "고객사를 모두 조회")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CONTRACTS_MANAGER')")
    public ResponseEntity<ResponseDto<PageDto<GetAllCustomerResponseDto>>> getAllCustomer(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetAllCustomerResponseDto> result = customerService.getAllCustomer(userPrincipal, page, size, sort);
        PageDto<GetAllCustomerResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "직원 세부정보 조회", description = "관리자, 담당자의 직원 세부정보 조회")
    @GetMapping(CUSTOMER_ID_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'CONTRACTS_MANAGER')")
    public ResponseEntity<ResponseDto<GetCustomerDetailResponseDto>> getCustomerDetailAdmin(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long customerId
    ) {
        ResponseDto<GetCustomerDetailResponseDto> response = customerService.getCustomerDetailAdmin(userPrincipal, customerId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "고객사 회원 탈퇴", description = "고객사 본인의 자발적 회원 탈퇴")
    @DeleteMapping(CUSTOMER_MY_INFO_API)
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<ResponseDto<Void>> deleteCustomer(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        ResponseDto<Void> response = customerService.deleteCustomer(userPrincipal);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

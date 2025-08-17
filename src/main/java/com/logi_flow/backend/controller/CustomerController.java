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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.CUSTOMER_API)
public class CustomerController {

    private final CustomerService customerService;

    private static final String CUSTOMER_MY_INFO_API = "/me";
    private static final String CUSTOMER_ID_API = "/{customerId}";
    private static final String CUSTOMER_STATUS_API = "/{customerId}/status";

    @PutMapping(CUSTOMER_MY_INFO_API)
    public ResponseEntity<ResponseDto<UpdateCustomerResponseDto>> updateCustomer(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody UpdateCustomerRequestDto dto
    ){
        ResponseDto<UpdateCustomerResponseDto> response = customerService.updateCustomer(userPrincipal, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping(CUSTOMER_MY_INFO_API)
    public ResponseEntity<ResponseDto<GetCustomerDetailResponseDto>> getCustomerDetail(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        ResponseDto<GetCustomerDetailResponseDto> response = customerService.getCustomerDetail(userPrincipal);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @PutMapping(CUSTOMER_ID_API)
    public ResponseEntity<ResponseDto<UpdateCustomerResponseDto>> updateCustomerAdmin(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long customerId,
            @Valid @RequestBody UpdateCustomerAdminRequestDto dto
    ){
        ResponseDto<UpdateCustomerResponseDto> response = customerService.updateCustomerAdmin(userPrincipal, customerId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @PutMapping(CUSTOMER_STATUS_API)
    public ResponseEntity<ResponseDto<UpdateCustomerStatusResponseDto>> updateCustomerStatus(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long customerId,
            @Valid @RequestBody UpdateCustomerStatusRequestDto dto
    ){
        ResponseDto<UpdateCustomerStatusResponseDto> response = customerService.updateCustomerStatus(userPrincipal, customerId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<PageDto<GetAllCustomerResponseDto>>> getAllCustomer(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {
        Page<GetAllCustomerResponseDto> result = customerService.getAllCustomer(userPrincipal, page, size, sort);
        PageDto<GetAllCustomerResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping(CUSTOMER_ID_API)
    public ResponseEntity<ResponseDto<GetCustomerDetailResponseDto>> getCustomerDetailAdmin(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long customerId
    ) {
        ResponseDto<GetCustomerDetailResponseDto> response = customerService.getCustomerDetailAdmin(userPrincipal, customerId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @DeleteMapping(CUSTOMER_ID_API)
    public ResponseEntity<ResponseDto<?>> deleteCustomer(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long customerId
    ) {
        ResponseDto<?> response = customerService.deleteCustomer(userPrincipal, customerId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

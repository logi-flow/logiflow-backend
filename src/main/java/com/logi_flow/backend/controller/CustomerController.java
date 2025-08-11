package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;

import com.logi_flow.backend.dto.customer.request.UpdateCustomerRequestDto;
import com.logi_flow.backend.dto.customer.request.UpdateCustomerStatusRequestDto;
import com.logi_flow.backend.dto.customer.response.GetAllCustomerResponseDto;
import com.logi_flow.backend.dto.customer.response.GetCustomerDetailResponseDto;
import com.logi_flow.backend.dto.customer.response.UpdateCustomerResponseDto;
import com.logi_flow.backend.dto.customer.response.UpdateCustomerStatusResponseDto;
import com.logi_flow.backend.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.CUSTOMER_API)
public class CustomerController {

    private final CustomerService customerService;

    private static final String CUSTOMER_ID_API = "/{customerId}";
    private static final String CUSTOMER_STATUS_API = "/{customerId}/status";

    @PutMapping(CUSTOMER_ID_API)
    public ResponseEntity<ResponseDto<UpdateCustomerResponseDto>> updateCustomer(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long customerId,
            @Valid @RequestBody UpdateCustomerRequestDto dto
    ){
        Long id = userPrincipal.getId();
        ResponseDto<UpdateCustomerResponseDto> response = customerService.updateCustomer(id, customerId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @PutMapping(CUSTOMER_STATUS_API)
    public ResponseEntity<ResponseDto<UpdateCustomerStatusResponseDto>> updateCustomerStatus(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long customerId,
            @Valid @RequestBody UpdateCustomerStatusRequestDto dto
    ){
        Long id = userPrincipal.getId();
        ResponseDto<UpdateCustomerStatusResponseDto> response = customerService.updateCustomerStatus(id, customerId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<GetAllCustomerResponseDto>> getAllCustomer(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        Long id = userPrincipal.getId();
        ResponseDto<GetAllCustomerResponseDto> response = customerService.getAllCustomer(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(CUSTOMER_ID_API)
    public ResponseEntity<ResponseDto<GetCustomerDetailResponseDto>> getCustomerDetail(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long customerId
    ) {
        Long id = userPrincipal.getId();
        ResponseDto<GetCustomerDetailResponseDto> response = customerService.getCustomerDetail(id, customerId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(CUSTOMER_ID_API)
    public ResponseEntity<ResponseDto<?>> deleteCustomer(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long customerId
    ) {
        Long id = userPrincipal.getId();
        ResponseDto<?> response = customerService.deleteCustomer(id, customerId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

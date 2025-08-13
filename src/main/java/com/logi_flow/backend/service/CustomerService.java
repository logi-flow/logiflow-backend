package com.logi_flow.backend.service;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.customer.request.UpdateCustomerAdminRequestDto;
import com.logi_flow.backend.dto.customer.request.UpdateCustomerRequestDto;
import com.logi_flow.backend.dto.customer.request.UpdateCustomerStatusRequestDto;
import com.logi_flow.backend.dto.customer.response.*;
import jakarta.validation.Valid;

public interface CustomerService {
    ResponseDto<UpdateCustomerResponseDto> updateCustomer(UserPrincipal userPrincipal, @Valid UpdateCustomerRequestDto dto);

    ResponseDto<GetCustomerDetailResponseDto> getCustomerDetail(UserPrincipal userPrincipal);

    ResponseDto<UpdateCustomerResponseDto> updateCustomerAdmin(UserPrincipal userPrincipal, Long customerId, @Valid UpdateCustomerAdminRequestDto dto);

    ResponseDto<UpdateCustomerStatusResponseDto> updateCustomerStatus(UserPrincipal userPrincipal, Long customerId, @Valid UpdateCustomerStatusRequestDto dto);

    ResponseDto<GetAllCustomerResponseDto> getAllCustomer(UserPrincipal userPrincipal);

    ResponseDto<GetCustomerDetailResponseDto> getCustomerDetailAdmin(UserPrincipal userPrincipal, Long customerId);

    ResponseDto<?> deleteCustomer(UserPrincipal userPrincipal, Long customerId);
}

package com.logi_flow.backend.service;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.customer.request.UpdateCustomerAdminRequestDto;
import com.logi_flow.backend.dto.customer.request.UpdateCustomerRequestDto;
import com.logi_flow.backend.dto.customer.request.UpdateCustomerStatusRequestDto;
import com.logi_flow.backend.dto.customer.response.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

public interface CustomerService {
    ResponseDto<UpdateCustomerResponseDto> updateCustomer(UserPrincipal userPrincipal, @Valid UpdateCustomerRequestDto dto);

    ResponseDto<GetCustomerDetailResponseDto> getCustomerDetail(UserPrincipal userPrincipal);

    ResponseDto<UpdateCustomerResponseDto> updateCustomerAdmin(UserPrincipal userPrincipal, Long customerId, @Valid UpdateCustomerAdminRequestDto dto);

    ResponseDto<UpdateCustomerStatusResponseDto> updateCustomerStatus(UserPrincipal userPrincipal, Long customerId, @Valid UpdateCustomerStatusRequestDto dto);

    Page<GetAllCustomerResponseDto> getAllCustomer(UserPrincipal userPrincipal, int page, int size, String sort);

    ResponseDto<GetCustomerDetailResponseDto> getCustomerDetailAdmin(UserPrincipal userPrincipal, Long customerId);
}

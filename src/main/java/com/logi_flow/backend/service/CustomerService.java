package com.logi_flow.backend.service;

import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.customer.request.UpdateCustomerAdminRequestDto;
import com.logi_flow.backend.dto.customer.request.UpdateCustomerRequestDto;
import com.logi_flow.backend.dto.customer.request.UpdateCustomerStatusRequestDto;
import com.logi_flow.backend.dto.customer.response.*;
import jakarta.validation.Valid;

public interface CustomerService {
    ResponseDto<UpdateCustomerResponseDto> updateCustomer(Long id, @Valid UpdateCustomerRequestDto dto);

    ResponseDto<GetCustomerDetailResponseDto> getCustomerDetail(Long id);

    ResponseDto<UpdateCustomerResponseDto> updateCustomerAdmin(Long id, Long customerId, @Valid UpdateCustomerAdminRequestDto dto);

    ResponseDto<UpdateCustomerStatusResponseDto> updateCustomerStatus(Long id, Long customerId, @Valid UpdateCustomerStatusRequestDto dto);

    ResponseDto<GetAllCustomerResponseDto> getAllCustomer(Long id);

    ResponseDto<GetCustomerDetailResponseDto> getCustomerDetailAdmin(Long id, Long customerId);

    ResponseDto<?> deleteCustomer(Long id, Long customerId);
}

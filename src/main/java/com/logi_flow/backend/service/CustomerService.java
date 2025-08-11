package com.logi_flow.backend.service;

import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.customer.request.UpdateCustomerRequestDto;
import com.logi_flow.backend.dto.customer.request.UpdateCustomerStatusRequestDto;
import com.logi_flow.backend.dto.customer.response.GetAllCustomerResponseDto;
import com.logi_flow.backend.dto.customer.response.GetCustomerDetailResponseDto;
import com.logi_flow.backend.dto.customer.response.UpdateCustomerResponseDto;
import com.logi_flow.backend.dto.customer.response.UpdateCustomerStatusResponseDto;
import jakarta.validation.Valid;

public interface CustomerService {
    ResponseDto<UpdateCustomerResponseDto> updateCustomer(Long id, Long customerId, @Valid UpdateCustomerRequestDto dto);

    ResponseDto<UpdateCustomerStatusResponseDto> updateCustomerStatus(Long id, Long customerId, @Valid UpdateCustomerStatusRequestDto dto);

    ResponseDto<GetAllCustomerResponseDto> getAllCustomer(Long id);

    ResponseDto<GetCustomerDetailResponseDto> getCustomerDetail(Long id, Long customerId);

    ResponseDto<?> deleteCustomer(Long id, Long customerId);
}

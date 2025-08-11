package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.customer.request.UpdateCustomerRequestDto;
import com.logi_flow.backend.dto.customer.request.UpdateCustomerStatusRequestDto;
import com.logi_flow.backend.dto.customer.response.GetAllCustomerResponseDto;
import com.logi_flow.backend.dto.customer.response.GetCustomerDetailResponseDto;
import com.logi_flow.backend.dto.customer.response.UpdateCustomerResponseDto;
import com.logi_flow.backend.dto.customer.response.UpdateCustomerStatusResponseDto;
import com.logi_flow.backend.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {
    @Override
    public ResponseDto<UpdateCustomerResponseDto> updateCustomer(Long id, Long customerId, UpdateCustomerRequestDto dto) {
        return null;
    }

    @Override
    public ResponseDto<UpdateCustomerStatusResponseDto> updateCustomerStatus(Long id, Long customerId, UpdateCustomerStatusRequestDto dto) {
        return null;
    }

    @Override
    public ResponseDto<GetAllCustomerResponseDto> getAllCustomer(Long id) {
        return null;
    }

    @Override
    public ResponseDto<GetCustomerDetailResponseDto> getCustomerDetail(Long id, Long customerId) {
        return null;
    }

    @Override
    public ResponseDto<?> deleteCustomer(Long id, Long customerId) {
        return null;
    }
}

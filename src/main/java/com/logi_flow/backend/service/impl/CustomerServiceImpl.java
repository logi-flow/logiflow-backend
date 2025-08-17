package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.customer.request.UpdateCustomerAdminRequestDto;
import com.logi_flow.backend.dto.customer.request.UpdateCustomerRequestDto;
import com.logi_flow.backend.dto.customer.request.UpdateCustomerStatusRequestDto;
import com.logi_flow.backend.dto.customer.response.*;
import com.logi_flow.backend.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {
    @Override
    public ResponseDto<UpdateCustomerResponseDto> updateCustomer(UserPrincipal userPrincipal, UpdateCustomerRequestDto dto) {
        return null;
    }

    @Override
    public ResponseDto<GetCustomerDetailResponseDto> getCustomerDetail(UserPrincipal userPrincipal) {
        return null;
    }

    @Override
    public ResponseDto<UpdateCustomerResponseDto> updateCustomerAdmin(UserPrincipal userPrincipal, Long customerId, UpdateCustomerAdminRequestDto dto) {
        return null;
    }

    @Override
    public ResponseDto<UpdateCustomerStatusResponseDto> updateCustomerStatus(UserPrincipal userPrincipal, Long customerId, UpdateCustomerStatusRequestDto dto) {
        return null;
    }

    @Override
    public Page<GetAllCustomerResponseDto> getAllCustomer(UserPrincipal userPrincipal, int page, int size, String sort) {
        return null;
    }

    @Override
    public ResponseDto<GetCustomerDetailResponseDto> getCustomerDetailAdmin(UserPrincipal userPrincipal, Long customerId) {
        return null;
    }

    @Override
    public ResponseDto<?> deleteCustomer(UserPrincipal userPrincipal, Long customerId) {
        return null;
    }
}

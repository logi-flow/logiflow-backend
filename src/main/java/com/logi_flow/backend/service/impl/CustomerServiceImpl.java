package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.constants.ResponseCode;
import com.logi_flow.backend.common.constants.ResponseMessage;
import com.logi_flow.backend.common.enums.CustomerStatus;
import com.logi_flow.backend.common.enums.user.UserRole;
import com.logi_flow.backend.common.util.DateUtils;
import com.logi_flow.backend.common.util.SortUtils;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.customer.request.UpdateCustomerAdminRequestDto;
import com.logi_flow.backend.dto.customer.request.UpdateCustomerRequestDto;
import com.logi_flow.backend.dto.customer.request.UpdateCustomerStatusRequestDto;
import com.logi_flow.backend.dto.customer.response.*;
import com.logi_flow.backend.entity.*;
import com.logi_flow.backend.repository.CustomerRepository;
import com.logi_flow.backend.repository.CustomerStatusLogRepository;
import com.logi_flow.backend.repository.CustomerUpdateLogRepository;
import com.logi_flow.backend.repository.UserRepository;
import com.logi_flow.backend.service.CustomerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final CustomerUpdateLogRepository customerUpdateLogRepository;
    private final CustomerStatusLogRepository customerStatusLogRepository;

    @Override
    public ResponseDto<UpdateCustomerResponseDto> updateCustomer(UserPrincipal userPrincipal, UpdateCustomerRequestDto dto) {
        UpdateCustomerResponseDto data = null;

        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Customer customer = customerRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        List<CustomerUpdateLog> logs = new ArrayList<>();

        if (!Objects.equals(dto.getFax(), customer.getFax())) {
            logs.add(buildUpdateLog(customer, user, username, "fax", customer.getFax(), dto.getFax()));
            customer.setFax(dto.getFax());
        }

        if (!Objects.equals(dto.getBusinessZipCode(), customer.getBusinessZipCode())) {
            logs.add(buildUpdateLog(customer, user, username, "business_zipCode", customer.getBusinessZipCode(), dto.getBusinessZipCode()));
            customer.setBusinessZipCode(dto.getBusinessZipCode());
        }

        if (!Objects.equals(dto.getBusinessAddress(), customer.getBusinessAddress())) {
            logs.add(buildUpdateLog(customer, user, username, "business_address", customer.getBusinessAddress(), dto.getBusinessAddress()));
            customer.setBusinessAddress(dto.getBusinessAddress());
        }

        if (dto.getBusinessAddressDetail() != null && !Objects.equals(dto.getBusinessAddressDetail(), customer.getBusinessAddressDetail())) {
            logs.add(buildUpdateLog(customer, user, username, "business_address_detail", customer.getBusinessAddressDetail(), dto.getBusinessAddressDetail()));
            customer.setBusinessAddressDetail(dto.getBusinessAddressDetail() );
        }

        if (dto.getChargePosition() != null && !Objects.equals(dto.getChargePosition(), customer.getChargePosition())) {
            logs.add(buildUpdateLog(customer, user, username, "charge_position", customer.getChargePosition(), dto.getChargePosition()));
            customer.setChargePosition(dto.getChargePosition());
        }

        if (dto.getChargeDepartment() != null && !Objects.equals(dto.getChargeDepartment(), customer.getChargeDepartment())) {
            logs.add(buildUpdateLog(customer, user, username, "charge_department", customer.getChargeDepartment(), dto.getChargeDepartment()));
            customer.setChargeDepartment(dto.getChargeDepartment());
        }

        if (dto.getChargeName() != null && !Objects.equals(dto.getChargeName(), customer.getChargeName())) {
            logs.add(buildUpdateLog(customer, user, username, "charge_name", customer.getChargeName(), dto.getChargeName()));
            customer.setChargeName(dto.getChargeName());
        }

        if (dto.getChargePhone() != null && !Objects.equals(dto.getChargePhone(), customer.getChargePhone())) {
            logs.add(buildUpdateLog(customer, user, username, "charge_phone", customer.getChargePhone(), dto.getChargePhone()));
            customer.setChargePhone(dto.getChargePhone());
        }

        if (dto.getChargeEmail() != null && !Objects.equals(dto.getChargeEmail(), customer.getChargeEmail())) {
            logs.add(buildUpdateLog(customer, user, username, "charge_email", customer.getChargeEmail(), dto.getChargeEmail()));
            customer.setChargeEmail(dto.getChargeEmail());
        }

        customerRepository.save(customer);
        customerUpdateLogRepository.saveAll(logs);

        data = UpdateCustomerResponseDto.builder()
                .id(customer.getId())
                .name(customer.getName())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<GetCustomerDetailResponseDto> getCustomerDetail(UserPrincipal userPrincipal) {
        GetCustomerDetailResponseDto data = null;

        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.USER_NOT_FOUND));
        Customer customer = customerRepository.findByUser(user)
                .orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.USER_NOT_FOUND));

        data = GetCustomerDetailResponseDto.builder()
                .id(customer.getId())
                .userId(customer.getUser().getId())
                .status(customer.getStatus())
                .name(customer.getName())
                .businessNumber(customer.getBusinessNumber())
                .representativeName(customer.getRepresentativeName())
                .businessType(customer.getBusinessType())
                .businessItems(customer.getBusinessItems())
                .telephone(customer.getTelephone())
                .email(customer.getUser().getEmail())
                .fax(customer.getFax())
                .businessZipCode(customer.getBusinessZipCode())
                .businessAddress(customer.getBusinessAddress())
                .businessAddressDetail(customer.getBusinessAddressDetail())
                .chargePosition(customer.getChargePosition())
                .chargeDepartment(customer.getChargeDepartment())
                .chargeName(customer.getChargeName())
                .chargePhone(customer.getChargePhone())
                .chargeEmail(customer.getChargeEmail())
                .createdAt(DateUtils.format(customer.getCreatedAt()))
                .updatedAt(DateUtils.format(customer.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<UpdateCustomerResponseDto> updateCustomerAdmin(UserPrincipal userPrincipal, Long customerId, UpdateCustomerAdminRequestDto dto) {
        UpdateCustomerResponseDto data = null;

        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        if(!user.getRole().getName().equals(UserRole.ADMIN)) {
            return ResponseDto.fail("FORBIDDEN", ResponseMessage.NO_PERMISSION);
        }

        List<CustomerUpdateLog> logs = new ArrayList<>();

        if (!Objects.equals(dto.getBusinessNumber(), customer.getBusinessNumber())) {
            logs.add(buildUpdateLog(customer, user, username, "business_number", customer.getBusinessNumber(), dto.getBusinessNumber()));
            customer.setBusinessNumber(dto.getBusinessNumber());
        }

        if (!Objects.equals(dto.getName(), customer.getName())) {
            logs.add(buildUpdateLog(customer, user, username, "name", customer.getName(), dto.getName()));
            customer.setName(dto.getName());
        }

        if (!Objects.equals(dto.getRepresentativeName(), customer.getRepresentativeName())) {
            logs.add(buildUpdateLog(customer, user, username, "representative_name", customer.getRepresentativeName(), dto.getRepresentativeName()));
            customer.setRepresentativeName(dto.getRepresentativeName());
        }

        if (!Objects.equals(dto.getBusinessType(), customer.getBusinessType())) {
            logs.add(buildUpdateLog(customer, user, username, "business_type", customer.getBusinessType(), dto.getBusinessType()));
            customer.setBusinessType(dto.getBusinessType());
        }

        if (!Objects.equals(dto.getBusinessItems(), customer.getBusinessItems())) {
            logs.add(buildUpdateLog(customer, user, username, "business_items", customer.getBusinessItems(), dto.getBusinessItems()));
            customer.setBusinessItems(dto.getBusinessItems());
        }

        if (!Objects.equals(dto.getTelephone(), customer.getTelephone())) {
            logs.add(buildUpdateLog(customer, user, username, "telephone", customer.getTelephone(), dto.getTelephone()));
            customer.setTelephone(dto.getTelephone());
        }

        if (!Objects.equals(dto.getFax(), customer.getFax())) {
            logs.add(buildUpdateLog(customer, user, username, "fax", customer.getFax(), dto.getFax()));
            customer.setFax(dto.getFax());
        }

        if (!Objects.equals(dto.getBusinessZipCode(), customer.getBusinessZipCode())) {
            logs.add(buildUpdateLog(customer, user, username, "business_zipCode", customer.getBusinessZipCode(), dto.getBusinessZipCode()));
            customer.setBusinessZipCode(dto.getBusinessZipCode());
        }

        if (!Objects.equals(dto.getBusinessAddress(), customer.getBusinessAddress())) {
            logs.add(buildUpdateLog(customer, user, username, "business_address", customer.getBusinessAddress(), dto.getBusinessAddress()));
            customer.setBusinessAddress(dto.getBusinessAddress());
        }

        if (dto.getBusinessAddressDetail() != null && !Objects.equals(dto.getBusinessAddressDetail(), customer.getBusinessAddressDetail())) {
            logs.add(buildUpdateLog(customer, user, username, "business_address_detail", customer.getBusinessAddressDetail(), dto.getBusinessAddressDetail()));
            customer.setBusinessAddressDetail(dto.getBusinessAddressDetail() );
        }

        if (dto.getChargePosition() != null && !Objects.equals(dto.getChargePosition(), customer.getChargePosition())) {
            logs.add(buildUpdateLog(customer, user, username, "charge_position", customer.getChargePosition(), dto.getChargePosition()));
            customer.setChargePosition(dto.getChargePosition());
        }

        if (dto.getChargeDepartment() != null && !Objects.equals(dto.getChargeDepartment(), customer.getChargeDepartment())) {
            logs.add(buildUpdateLog(customer, user, username, "charge_department", customer.getChargeDepartment(), dto.getChargeDepartment()));
            customer.setChargeDepartment(dto.getChargeDepartment());
        }

        if (dto.getChargeName() != null && !Objects.equals(dto.getChargeName(), customer.getChargeName())) {
            logs.add(buildUpdateLog(customer, user, username, "charge_name", customer.getChargeName(), dto.getChargeName()));
            customer.setChargeName(dto.getChargeName());
        }

        if (dto.getChargePhone() != null && !Objects.equals(dto.getChargePhone(), customer.getChargePhone())) {
            logs.add(buildUpdateLog(customer, user, username, "charge_phone", customer.getChargePhone(), dto.getChargePhone()));
            customer.setChargePhone(dto.getChargePhone());
        }

        if (dto.getChargeEmail() != null && !Objects.equals(dto.getChargeEmail(), customer.getChargeEmail())) {
            logs.add(buildUpdateLog(customer, user, username, "charge_email", customer.getChargeEmail(), dto.getChargeEmail()));
            customer.setChargeEmail(dto.getChargeEmail());
        }

        customerRepository.save(customer);
        customerUpdateLogRepository.saveAll(logs);

        data = UpdateCustomerResponseDto.builder()
                .id(customer.getId())
                .name(customer.getName())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<UpdateCustomerStatusResponseDto> updateCustomerStatus(UserPrincipal userPrincipal, Long customerId, UpdateCustomerStatusRequestDto dto) {
        UpdateCustomerStatusResponseDto data = null;

        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        if(!user.getRole().getName().equals(UserRole.ADMIN)) {
            return ResponseDto.fail("FORBIDDEN", ResponseMessage.NO_PERMISSION);
        }

        CustomerStatus prevStatus = customer.getStatus();

        if (dto.getStatus() != prevStatus) {
            customer.setStatus(dto.getStatus());
        }

        Customer updatedCustomer = customerRepository.save(customer);

        CustomerStatusLog customerStatusLog = CustomerStatusLog.builder()
                .customer(customer)
                .user(user)
                .changedByUsername(username)
                .changeReason(dto.getChangedReason())
                .prevStatus(prevStatus)
                .newStatus(updatedCustomer.getStatus())
                .build();

        customerStatusLogRepository.save(customerStatusLog);

        data = UpdateCustomerStatusResponseDto.builder()
                .id(updatedCustomer.getId())
                .status(updatedCustomer.getStatus())
                .changedBy(user.getId())
                .changedByUsername(username)
                .changedReason(customerStatusLog.getChangeReason())
                .prevStatus(prevStatus)
                .newStatus(updatedCustomer.getStatus())
                .createdAt(DateUtils.format(updatedCustomer.getCreatedAt()))
                .updatedAt(DateUtils.format(updatedCustomer.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public Page<GetAllCustomerResponseDto> getAllCustomer(UserPrincipal userPrincipal, int page, int size, String sort) {
        Page<GetAllCustomerResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<Customer> customers = customerRepository.findAll(pageable);

        data = customers.map(this::toGetAllCustomerResponseDto);

        return data;
    }

    @Override
    public ResponseDto<GetCustomerDetailResponseDto> getCustomerDetailAdmin(UserPrincipal userPrincipal, Long customerId) {
        GetCustomerDetailResponseDto data = null;

        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.USER_NOT_FOUND));

        if(!user.getRole().getName().equals(UserRole.ADMIN)) {
            return ResponseDto.fail("FORBIDDEN", ResponseMessage.NO_PERMISSION);
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        data = GetCustomerDetailResponseDto.builder()
                .id(customer.getId())
                .userId(customer.getUser().getId())
                .status(customer.getStatus())
                .name(customer.getName())
                .businessNumber(customer.getBusinessNumber())
                .representativeName(customer.getRepresentativeName())
                .businessType(customer.getBusinessType())
                .businessItems(customer.getBusinessItems())
                .telephone(customer.getTelephone())
                .email(customer.getUser().getEmail())
                .fax(customer.getFax())
                .businessZipCode(customer.getBusinessZipCode())
                .businessAddress(customer.getBusinessAddress())
                .businessAddressDetail(customer.getBusinessAddressDetail())
                .chargePosition(customer.getChargePosition())
                .chargeDepartment(customer.getChargeDepartment())
                .chargeName(customer.getChargeName())
                .chargePhone(customer.getChargePhone())
                .chargeEmail(customer.getChargeEmail())
                .createdAt(DateUtils.format(customer.getCreatedAt()))
                .updatedAt(DateUtils.format(customer.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    private CustomerUpdateLog buildUpdateLog(Customer customer, User user, String username, String type, String prevData, String newData) {
        return CustomerUpdateLog.builder()
                .customer(customer)
                .user(user)
                .changedByUsername(username)
                .type(type)
                .prevData(String.valueOf(prevData))
                .newData(String.valueOf(newData))
                .build();
    }

    private GetAllCustomerResponseDto toGetAllCustomerResponseDto (Customer customer) {
        return GetAllCustomerResponseDto.builder()
                .id(customer.getId())
                .userId(customer.getUser().getId())
                .name(customer.getName())
                .status(customer.getStatus())
                .businessNumber(customer.getBusinessNumber())
                .telephone(customer.getTelephone())
                .email(customer.getUser().getEmail())
                .createdAt(DateUtils.format(customer.getCreatedAt()))
                .updatedAt(DateUtils.format(customer.getUpdatedAt()))
                .build();
    }
}

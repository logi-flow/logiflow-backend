package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.constants.ResponseCode;
import com.logi_flow.backend.common.constants.ResponseMessage;
import com.logi_flow.backend.common.util.DateUtils;
import com.logi_flow.backend.common.util.SortUtils;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.destinationSite.request.CreateDestinationSiteRequestDto;
import com.logi_flow.backend.dto.destinationSite.request.UpdateDestinationSiteRequestDto;
import com.logi_flow.backend.dto.destinationSite.response.CreateDestinationSiteResponseDto;
import com.logi_flow.backend.dto.destinationSite.response.GetAllDestinationSiteResponseDto;
import com.logi_flow.backend.dto.destinationSite.response.UpdateDestinationSiteResponseDto;
import com.logi_flow.backend.entity.Customer;
import com.logi_flow.backend.entity.DestinationSite;
import com.logi_flow.backend.entity.User;
import com.logi_flow.backend.repository.CustomerRepository;
import com.logi_flow.backend.repository.DestinationSiteRepository;
import com.logi_flow.backend.repository.UserRepository;
import com.logi_flow.backend.service.DestinationSiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class DestinationSiteServiceImpl implements DestinationSiteService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final DestinationSiteRepository destinationSiteRepository;

    @Override
    public ResponseDto<CreateDestinationSiteResponseDto> createDestinationSite(UserPrincipal userPrincipal, CreateDestinationSiteRequestDto dto) {
        CreateDestinationSiteResponseDto data = null;

        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.USER_NOT_FOUND));
        Customer customer = customerRepository.findByUser(user)
                .orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.USER_NOT_FOUND));

        DestinationSite destinationSite = DestinationSite.builder()
                .customer(customer)
                .name(dto.getName())
                .zipCode(dto.getZipCode())
                .phoneNumber(dto.getPhoneNumber())
                .address(dto.getAddress())
                .addressDetail(dto.getAddressDetail())
                .build();

        destinationSiteRepository.save(destinationSite);

        data = CreateDestinationSiteResponseDto.builder()
                .id(destinationSite.getId())
                .customerId(destinationSite.getCustomer().getId())
                .name(destinationSite.getName())
                .zipcode(destinationSite.getZipCode())
                .phoneNumber(destinationSite.getPhoneNumber())
                .address(destinationSite.getAddress())
                .addressDetail(destinationSite.getAddressDetail())
                .createdAt(DateUtils.format(destinationSite.getCreatedAt()))
                .updatedAt(DateUtils.format(destinationSite.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<UpdateDestinationSiteResponseDto> updateDestinationSite(UserPrincipal userPrincipal, Long destinationSiteId, UpdateDestinationSiteRequestDto dto) throws AccessDeniedException {
        UpdateDestinationSiteResponseDto data = null;

        DestinationSite destinationSite = destinationSiteRepository.findById(destinationSiteId)
                .orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.FAILED));

        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Customer customer = customerRepository.findByUser(user)
                .orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.USER_NOT_FOUND));

        if (!destinationSite.getCustomer().getId().equals(customer.getId())) {
            throw new AccessDeniedException(ResponseMessage.NO_PERMISSION);
        }

        if(!dto.getName().equals(destinationSite.getName())) {
            destinationSite.setName(dto.getName());
        }

        if(!dto.getZipCode().equals(destinationSite.getZipCode())) {
            destinationSite.setZipCode(dto.getZipCode());
        }

        if(!dto.getAddress().equals(destinationSite.getAddress())) {
            destinationSite.setAddress(dto.getAddress());
        }

        if(!dto.getAddressDetail().equals(destinationSite.getAddressDetail())) {
            destinationSite.setAddressDetail(dto.getAddressDetail());
        }

        DestinationSite updatedDestinationSite = destinationSiteRepository.save(destinationSite);

        data = UpdateDestinationSiteResponseDto.builder()
                .id(updatedDestinationSite.getId())
                .customerId(updatedDestinationSite.getCustomer().getId())
                .name(updatedDestinationSite.getName())
                .zipcode(updatedDestinationSite.getZipCode())
                .phoneNumber(destinationSite.getPhoneNumber())
                .address(updatedDestinationSite.getAddress())
                .addressDetail(updatedDestinationSite.getAddressDetail())
                .createdAt(DateUtils.format(updatedDestinationSite.getCreatedAt()))
                .updatedAt(DateUtils.format(updatedDestinationSite.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public Page<GetAllDestinationSiteResponseDto> getAllDestinationSite(UserPrincipal userPrincipal, int page, int size, String sort) {
        Page<GetAllDestinationSiteResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<DestinationSite> destinationSites = destinationSiteRepository.findAll(pageable);

        data = destinationSites.map(this::toGetAllDestinationSiteResponseDto);

        return data;
    }

    @Override
    public ResponseDto<Void> deleteDestinationSite(UserPrincipal userPrincipal, Long destinationSiteId) throws AccessDeniedException {
        DestinationSite destinationSite = destinationSiteRepository.findById(destinationSiteId)
                .orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.FAILED));

        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Customer customer = customerRepository.findByUser(user)
                .orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.USER_NOT_FOUND));

        if (!destinationSite.getCustomer().getId().equals(customer.getId())) {
            throw new AccessDeniedException(ResponseMessage.NO_PERMISSION);
        }

        destinationSiteRepository.delete(destinationSite);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    private GetAllDestinationSiteResponseDto toGetAllDestinationSiteResponseDto(DestinationSite destinationSite) {
        return GetAllDestinationSiteResponseDto.builder()
                .id(destinationSite.getId())
                .customerId(destinationSite.getCustomer().getId())
                .name(destinationSite.getName())
                .zipcode(destinationSite.getZipCode())
                .phoneNumber(destinationSite.getPhoneNumber())
                .address(destinationSite.getAddress())
                .addressDetail(destinationSite.getAddressDetail())
                .createdAt(DateUtils.format(destinationSite.getCreatedAt()))
                .updatedAt(DateUtils.format(destinationSite .getUpdatedAt()))
                .build();
    }
}

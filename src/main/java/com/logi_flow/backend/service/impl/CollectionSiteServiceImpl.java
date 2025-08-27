package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.constants.ResponseCode;
import com.logi_flow.backend.common.constants.ResponseMessage;
import com.logi_flow.backend.common.util.DateUtils;
import com.logi_flow.backend.common.util.SortUtils;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.collectionSite.request.CreateCollectionSiteRequestDto;
import com.logi_flow.backend.dto.collectionSite.request.UpdateCollectionSiteRequestDto;
import com.logi_flow.backend.dto.collectionSite.response.CreateCollectionSiteResponseDto;
import com.logi_flow.backend.dto.collectionSite.response.GetAllCollectionSiteResponseDto;
import com.logi_flow.backend.dto.collectionSite.response.UpdateCollectionSiteResponseDto;
import com.logi_flow.backend.entity.CollectionSite;
import com.logi_flow.backend.entity.Customer;
import com.logi_flow.backend.entity.DestinationSite;
import com.logi_flow.backend.entity.User;
import com.logi_flow.backend.repository.CollectionSiteRepository;
import com.logi_flow.backend.repository.CustomerRepository;
import com.logi_flow.backend.repository.UserRepository;
import com.logi_flow.backend.service.CollectionSiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class CollectionSiteServiceImpl implements CollectionSiteService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final CollectionSiteRepository collectionSiteRepository;

    @Override
    public ResponseDto<CreateCollectionSiteResponseDto> createCollectionSite(UserPrincipal userPrincipal, CreateCollectionSiteRequestDto dto) {
        CreateCollectionSiteResponseDto data = null;

        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.USER_NOT_FOUND));
        Customer customer = customerRepository.findByUser(user)
                .orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.USER_NOT_FOUND));

        CollectionSite collectionSite = CollectionSite.builder()
                .customer(customer)
                .name(dto.getName())
                .zipCode(dto.getZipCode())
                .phoneNumber(dto.getPhoneNumber())
                .address(dto.getAddress())
                .addressDetail(dto.getAddressDetail())
                .build();

        collectionSiteRepository.save(collectionSite);

        data = CreateCollectionSiteResponseDto.builder()
                .id(collectionSite.getId())
                .customerId(collectionSite.getCustomer().getId())
                .name(collectionSite.getName())
                .zipcode(collectionSite.getZipCode())
                .phoneNumber(collectionSite.getPhoneNumber())
                .address(collectionSite.getAddress())
                .addressDetail(collectionSite.getAddressDetail())
                .createdAt(DateUtils.format(collectionSite.getCreatedAt()))
                .updatedAt(DateUtils.format(collectionSite.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<UpdateCollectionSiteResponseDto> updateCollectionSite(UserPrincipal userPrincipal, Long collectionSiteId, UpdateCollectionSiteRequestDto dto) throws AccessDeniedException {
        UpdateCollectionSiteResponseDto data = null;

        CollectionSite collectionSite = collectionSiteRepository.findById(collectionSiteId)
                .orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.FAILED));

        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Customer customer = customerRepository.findByUser(user)
                .orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.USER_NOT_FOUND));

        if (!collectionSite.getCustomer().getId().equals(customer.getId())) {
            throw new AccessDeniedException(ResponseMessage.NO_PERMISSION);
        }

        if(!dto.getName().equals(collectionSite.getName())) {
            collectionSite.setName(dto.getName());
        }

        if(!dto.getZipCode().equals(collectionSite.getZipCode())) {
            collectionSite.setZipCode(dto.getZipCode());
        }

        if(!dto.getAddress().equals(collectionSite.getAddress())) {
            collectionSite.setAddress(dto.getAddress());
        }

        if(!dto.getAddressDetail().equals(collectionSite.getAddressDetail())) {
            collectionSite.setAddressDetail(dto.getAddressDetail());
        }

        CollectionSite updatedCollectionSite = collectionSiteRepository.save(collectionSite);

        data = UpdateCollectionSiteResponseDto.builder()
                .id(updatedCollectionSite.getId())
                .customerId(updatedCollectionSite.getCustomer().getId())
                .name(updatedCollectionSite.getName())
                .zipcode(updatedCollectionSite.getZipCode())
                .phoneNumber(collectionSite.getPhoneNumber())
                .address(updatedCollectionSite.getAddress())
                .addressDetail(updatedCollectionSite.getAddressDetail())
                .createdAt(DateUtils.format(updatedCollectionSite.getCreatedAt()))
                .updatedAt(DateUtils.format(updatedCollectionSite.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public Page<GetAllCollectionSiteResponseDto> getAllCollectionSite(UserPrincipal userPrincipal, int page, int size, String sort) {
        Page<GetAllCollectionSiteResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<CollectionSite> collectionSites = collectionSiteRepository.findAll(pageable);

        data = collectionSites.map(this::toGetAllCollectionSiteResponseDto);

        return data;
    }

    @Override
    public ResponseDto<Void> deleteCollectionSite(UserPrincipal userPrincipal, Long collectionSiteId) throws AccessDeniedException {
        CollectionSite collectionSite = collectionSiteRepository.findById(collectionSiteId)
                .orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.FAILED));

        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Customer customer = customerRepository.findByUser(user)
                .orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.USER_NOT_FOUND));

        if (!collectionSite.getCustomer().getId().equals(customer.getId())) {
            throw new AccessDeniedException(ResponseMessage.NO_PERMISSION);
        }

        collectionSiteRepository.delete(collectionSite);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    private GetAllCollectionSiteResponseDto toGetAllCollectionSiteResponseDto(CollectionSite collectionSite) {
        return GetAllCollectionSiteResponseDto.builder()
                .id(collectionSite.getId())
                .customerId(collectionSite.getCustomer().getId())
                .name(collectionSite.getName())
                .zipcode(collectionSite.getZipCode())
                .phoneNumber(collectionSite.getPhoneNumber())
                .address(collectionSite.getAddress())
                .addressDetail(collectionSite.getAddressDetail())
                .createdAt(DateUtils.format(collectionSite.getCreatedAt()))
                .updatedAt(DateUtils.format(collectionSite.getUpdatedAt()))
                .build();
    }
}

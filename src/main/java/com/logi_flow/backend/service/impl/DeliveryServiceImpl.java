package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.constants.ResponseCode;
import com.logi_flow.backend.common.constants.ResponseMessage;
import com.logi_flow.backend.common.enums.DeliveryStatus;
import com.logi_flow.backend.common.util.SortUtils;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.delivery.request.CreateDeliveryRequestDto;
import com.logi_flow.backend.dto.delivery.request.UpdateDeliveryRequestDto;
import com.logi_flow.backend.dto.delivery.request.UpdateDeliveryStatusRequestDto;
import com.logi_flow.backend.dto.delivery.request.UpdateIsHiddenRequestDto;
import com.logi_flow.backend.dto.delivery.response.*;
import com.logi_flow.backend.entity.*;
import com.logi_flow.backend.repository.*;
import com.logi_flow.backend.service.DeliveryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final ContractRepository contractRepository;
    private final DeliveryRepository deliveryRepository;
    private final DeliveryUpdateLogRepository deliveryUpdateLogRepository;

    // 상태 > 승인되면 finalFee ~ isOverParcel 까지 계산해서 자동으로 넣기
    @Override
    public ResponseDto<CreateDeliveryResponseDto> createDelivery(CreateDeliveryRequestDto dto, UserPrincipal userPrincipal) {
        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Customer customer = customerRepository.findByUser(user).orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Contract contract = contractRepository.findById(dto.getContractId()).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        Delivery newDelivery = Delivery.builder()
                .contract(contract)
                .customer(customer)
                .requestDate(dto.getRequestDate())
                .item(dto.getItem())
                .weight(dto.getWeight())
                .message(dto.getMessage())
                .isHidden(dto.isHidden())
                .status(dto.getStatus())
                .pickupName(dto.getPickupName())
                .pickupPhone(dto.getPickupPhone())
                .pickupZipCode(dto.getPickupZipcode())
                .pickupAddress(dto.getPickupAddress())
                .pickupAddressDetail(dto.getPickupAddressDetail())
                .recipientName(dto.getRecipientName())
                .recipientPhone(dto.getRecipientPhone())
                .recipientZipcode(dto.getRecipientZipcode())
                .recipientAddress(dto.getRecipientAddress())
                .recipientAddressDetail(dto.getRecipientAddressDetail())
                .finalFee(0)
                .overWeightFee(0)
                .overParcelFee(0)
                .isOverWeight(false)
                .isOverParcel(false)
                .build();

        deliveryRepository.save(newDelivery);

        CreateDeliveryResponseDto data = CreateDeliveryResponseDto.builder()
                .id(newDelivery.getId())
                .contractId(newDelivery.getContract().getId())
                .customerId(newDelivery.getCustomer().getId())
                .requestDate(newDelivery.getRequestDate())
                .item(newDelivery.getItem())
                .weight(newDelivery.getWeight())
                .message(newDelivery.getMessage())
                .isHidden(newDelivery.isHidden())
                .status(newDelivery.getStatus())
                .pickupName(newDelivery.getPickupName())
                .pickupPhone(newDelivery.getPickupPhone())
                .pickupZipcode(newDelivery.getPickupZipCode())
                .pickupAddress(newDelivery.getPickupAddress())
                .pickupAddressDetail(newDelivery.getPickupAddressDetail())
                .recipientName(newDelivery.getRecipientName())
                .recipientPhone(newDelivery.getRecipientPhone())
                .recipientZipcode(newDelivery.getRecipientZipcode())
                .recipientAddress(newDelivery.getRecipientAddress())
                .recipientAddressDetail(newDelivery.getRecipientAddressDetail())
                .finalFee(newDelivery.getFinalFee())
                .overWeightFee(newDelivery.getOverWeightFee())
                .overParcelFee(newDelivery.getOverParcelFee())
                .isOverWeight(newDelivery.isOverWeight())
                .isOverParcel(newDelivery.isOverParcel())
                .createdAt(newDelivery.getCreatedAt())
                .updatedAt(newDelivery.getUpdatedAt())
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public Page<GetAllDeliveryResponseDto> getAllDelivery(int page, int size, String sort) {
        Page<GetAllDeliveryResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<Delivery> deliveries = deliveryRepository.findAll(pageable);

        data = deliveries.map(this::toGetAllDeliveryResponseDto);

        return data;
    }

    @Override
    public ResponseDto<GetDeliveryDetailResponseDto> getDelivery(Long deliveryId) {

        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        GetDeliveryDetailResponseDto data = GetDeliveryDetailResponseDto.builder()
                .id(delivery.getId())
                .contractId(delivery.getContract().getId())
                .customerId(delivery.getCustomer().getId())
                .requestDate(delivery.getRequestDate())
                .item(delivery.getItem())
                .weight(delivery.getWeight())
                .message(delivery.getMessage())
                .isHidden(delivery.isHidden())
                .status(delivery.getStatus())
                .pickupName(delivery.getPickupName())
                .pickupPhone(delivery.getPickupPhone())
                .pickupZipcode(delivery.getPickupZipCode())
                .pickupAddress(delivery.getPickupAddress())
                .pickupAddressDetail(delivery.getPickupAddressDetail())
                .recipientName(delivery.getRecipientName())
                .recipientPhone(delivery.getRecipientPhone())
                .recipientZipcode(delivery.getRecipientZipcode())
                .recipientAddress(delivery.getRecipientAddress())
                .recipientAddressDetail(delivery.getRecipientAddressDetail())
                .finalFee(delivery.getFinalFee())
                .overWeightFee(delivery.getOverWeightFee())
                .overParcelFee(delivery.getOverParcelFee())
                .isOverWeight(delivery.isOverWeight())
                .isOverParcel(delivery.isOverParcel())
                .createdAt(delivery.getCreatedAt())
                .updatedAt(delivery.getUpdatedAt())
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public Page<GetAllDeliveryResponseDto> getMyDeliveries(UserPrincipal userPrincipal, int page, int size, String sort) {
        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Customer customer = customerRepository.findByUser(user).orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Page<GetAllDeliveryResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<Delivery> deliveries = deliveryRepository.findByCustomerId(customer.getId(), pageable);

        data = deliveries.map(this::toGetAllDeliveryResponseDto);

        return data;
    }

    @Override
    public ResponseDto<UpdateDeliveryResponseDto> updateDeliveryIsHidden(Long deliveryId, UpdateIsHiddenRequestDto dto, UserPrincipal userPrincipal) {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Customer customer = customerRepository.findByUser(user).orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.USER_NOT_FOUND));

        if(!delivery.getCustomer().getId().equals(customer.getId())) {
            return ResponseDto.fail("FORBIDDEN", ResponseMessage.NO_PERMISSION);
        }

        if(dto.isHidden() != delivery.isHidden()) {
            delivery.setHidden(dto.isHidden());
        }

        Delivery updatedDelivery = deliveryRepository.save(delivery);

        UpdateDeliveryResponseDto data = UpdateDeliveryResponseDto.builder()
                .id(updatedDelivery.getId())
                .contractId(updatedDelivery.getContract().getId())
                .customerId(updatedDelivery.getCustomer().getId())
                .requestDate(updatedDelivery.getRequestDate())
                .item(updatedDelivery.getItem())
                .weight(updatedDelivery.getWeight())
                .message(updatedDelivery.getMessage())
                .isHidden(updatedDelivery.isHidden())
                .status(updatedDelivery.getStatus())
                .pickupName(updatedDelivery.getPickupName())
                .pickupPhone(updatedDelivery.getPickupPhone())
                .pickupZipcode(updatedDelivery.getPickupZipCode())
                .pickupAddress(updatedDelivery.getPickupAddress())
                .pickupAddressDetail(updatedDelivery.getPickupAddressDetail())
                .recipientName(updatedDelivery.getRecipientName())
                .recipientPhone(updatedDelivery.getRecipientPhone())
                .recipientZipcode(updatedDelivery.getRecipientZipcode())
                .recipientAddress(updatedDelivery.getRecipientAddress())
                .recipientAddressDetail(updatedDelivery.getRecipientAddressDetail())
                .finalFee(updatedDelivery.getFinalFee())
                .overWeightFee(updatedDelivery.getOverWeightFee())
                .overParcelFee(updatedDelivery.getOverParcelFee())
                .isOverWeight(updatedDelivery.isOverWeight())
                .isOverParcel(updatedDelivery.isOverParcel())
                .createdAt(updatedDelivery.getCreatedAt())
                .updatedAt(updatedDelivery.getUpdatedAt())
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<UpdateDeliveryResponseDto> updateDelivery(Long deliveryId, UpdateDeliveryRequestDto dto, UserPrincipal userPrincipal) {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Customer customer = customerRepository.findByUser(user).orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.USER_NOT_FOUND));

        if(!delivery.getCustomer().getId().equals(customer.getId())) {
            return ResponseDto.fail("FORBIDDEN", ResponseMessage.NO_PERMISSION);
        }

        List<DeliveryUpdateLog> logs = new ArrayList<>();

        if(!dto.getRequestDate().equals(delivery.getRequestDate())) {
            logs.add(buildDeliveryLog(delivery, user, username, "request_date", delivery.getRequestDate().toString(), dto.getRequestDate().toString()));
            delivery.setRequestDate(dto.getRequestDate());
        }

        if(!dto.getItem().equals(delivery.getItem())) {
            logs.add(buildDeliveryLog(delivery, user, username, "item", delivery.getItem(), dto.getItem()));
            delivery.setItem(dto.getItem());
        }

        if(dto.getWeight().compareTo(delivery.getWeight()) != 0) {
            logs.add(buildDeliveryLog(delivery, user, username, "weight", delivery.getWeight().toString(), dto.getWeight().toString()));
            delivery.setWeight(dto.getWeight());
        }

        if(!dto.getMessage().equals(delivery.getMessage())) {
            logs.add(buildDeliveryLog(delivery, user, username, "message", delivery.getMessage(), dto.getMessage()));
            delivery.setMessage(dto.getMessage());
        }

        if(!dto.getPickupName().equals(delivery.getPickupName())) {
            logs.add(buildDeliveryLog(delivery, user, username, "pickup_name", delivery.getPickupName(), dto.getPickupName()));
            delivery.setPickupName(dto.getPickupName());
        }

        if(!dto.getPickupPhone().equals(delivery.getPickupPhone())) {
            logs.add(buildDeliveryLog(delivery, user, username, "pickup_phone", dto.getPickupPhone(), dto.getPickupPhone()));
            delivery.setPickupPhone(dto.getPickupPhone());
        }

        if(!dto.getPickupZipcode().equals(delivery.getPickupZipCode())) {
            logs.add(buildDeliveryLog(delivery, user, username, "pickup_zipcode", dto.getPickupZipcode(), dto.getPickupZipcode()));
            delivery.setPickupZipCode(dto.getPickupZipcode());
        }

        if(!dto.getPickupAddress().equals(delivery.getPickupAddress())) {
            logs.add(buildDeliveryLog(delivery, user, username, "pickup_address", dto.getPickupAddress(), dto.getPickupAddress()));
            delivery.setPickupAddress(dto.getPickupAddress());
        }

        if(dto.getPickupAddressDetail() != null && !dto.getPickupAddressDetail().equals(delivery.getPickupAddressDetail())) {
            logs.add(buildDeliveryLog(delivery, user, username, "pickup_address_detail", dto.getPickupAddressDetail(), dto.getPickupAddressDetail()));
            delivery.setPickupAddressDetail(dto.getPickupAddressDetail());
        }

        if(!dto.getRecipientName().equals(delivery.getRecipientName())) {
            logs.add(buildDeliveryLog(delivery, user, username, "recipient_name", delivery.getRecipientName(), dto.getRecipientName()));
            delivery.setRecipientName(dto.getRecipientName());
        }

        if(!dto.getRecipientPhone().equals(delivery.getRecipientPhone())) {
            logs.add(buildDeliveryLog(delivery, user, username, "recipient_phone", delivery.getRecipientPhone(), dto.getRecipientPhone()));
            delivery.setRecipientPhone(dto.getRecipientPhone());
        }

        if(!dto.getRecipientZipcode().equals(delivery.getRecipientZipcode())) {
            logs.add(buildDeliveryLog(delivery, user, username, "recipient_zipcode", dto.getRecipientZipcode(), dto.getRecipientZipcode()));
            delivery.setRecipientZipcode(dto.getRecipientZipcode());
        }

        if(!dto.getRecipientAddress().equals(delivery.getRecipientAddress())) {
            logs.add(buildDeliveryLog(delivery, user, username, "recipient_address", dto.getRecipientAddress(), dto.getRecipientAddress()));
            delivery.setRecipientAddress(dto.getRecipientAddress());
        }

        if(dto.getRecipientAddressDetail() != null && !dto.getRecipientAddressDetail().equals(delivery.getRecipientAddressDetail())) {
            logs.add(buildDeliveryLog(delivery, user, username, "recipient_address_detail", dto.getRecipientAddressDetail(), dto.getRecipientAddressDetail()));
            delivery.setRecipientAddressDetail(dto.getRecipientAddressDetail());
        }

        Delivery updatedDelivery = deliveryRepository.save(delivery);

        if(!logs.isEmpty()) {
            deliveryUpdateLogRepository.saveAll(logs);
        }

        UpdateDeliveryResponseDto data = UpdateDeliveryResponseDto.builder()
                .id(updatedDelivery.getId())
                .contractId(updatedDelivery.getContract().getId())
                .customerId(updatedDelivery.getCustomer().getId())
                .requestDate(updatedDelivery.getRequestDate())
                .item(updatedDelivery.getItem())
                .weight(updatedDelivery.getWeight())
                .message(updatedDelivery.getMessage())
                .isHidden(updatedDelivery.isHidden())
                .status(updatedDelivery.getStatus())
                .pickupName(updatedDelivery.getPickupName())
                .pickupPhone(updatedDelivery.getPickupPhone())
                .pickupZipcode(updatedDelivery.getPickupZipCode())
                .pickupAddress(updatedDelivery.getPickupAddress())
                .pickupAddressDetail(updatedDelivery.getPickupAddressDetail())
                .recipientName(updatedDelivery.getRecipientName())
                .recipientPhone(updatedDelivery.getRecipientPhone())
                .recipientZipcode(updatedDelivery.getRecipientZipcode())
                .recipientAddress(updatedDelivery.getRecipientAddress())
                .recipientAddressDetail(updatedDelivery.getRecipientAddressDetail())
                .finalFee(updatedDelivery.getFinalFee())
                .overWeightFee(updatedDelivery.getOverWeightFee())
                .overParcelFee(updatedDelivery.getOverParcelFee())
                .isOverWeight(updatedDelivery.isOverWeight())
                .isOverParcel(updatedDelivery.isOverParcel())
                .createdAt(updatedDelivery.getCreatedAt())
                .updatedAt(updatedDelivery.getUpdatedAt())
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    // 여기해야함
    @Override
    public ResponseDto<UpdateDeliveryResponseDto> updateDeliveryStatus(Long deliveryId, UpdateDeliveryStatusRequestDto dto, UserPrincipal userPrincipal) {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));
        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.USER_NOT_FOUND));

        if(!user.getRole().getName().equals("ADMIN")) {
            return ResponseDto.fail("FORBIDDEN", ResponseMessage.NO_PERMISSION);
        }

        if(dto.getStatus() != delivery.getStatus()) {
            delivery.setStatus(dto.getStatus());
        }

        Delivery updatedDelivery = deliveryRepository.save(delivery);

        UpdateDeliveryResponseDto data = UpdateDeliveryResponseDto.builder()
                .id(updatedDelivery.getId())
                .contractId(updatedDelivery.getContract().getId())
                .customerId(updatedDelivery.getCustomer().getId())
                .requestDate(updatedDelivery.getRequestDate())
                .item(updatedDelivery.getItem())
                .weight(updatedDelivery.getWeight())
                .message(updatedDelivery.getMessage())
                .isHidden(updatedDelivery.isHidden())
                .status(updatedDelivery.getStatus())
                .pickupName(updatedDelivery.getPickupName())
                .pickupPhone(updatedDelivery.getPickupPhone())
                .pickupZipcode(updatedDelivery.getPickupZipCode())
                .pickupAddress(updatedDelivery.getPickupAddress())
                .pickupAddressDetail(updatedDelivery.getPickupAddressDetail())
                .recipientName(updatedDelivery.getRecipientName())
                .recipientPhone(updatedDelivery.getRecipientPhone())
                .recipientZipcode(updatedDelivery.getRecipientZipcode())
                .recipientAddress(updatedDelivery.getRecipientAddress())
                .recipientAddressDetail(updatedDelivery.getRecipientAddressDetail())
                .finalFee(updatedDelivery.getFinalFee())
                .overWeightFee(updatedDelivery.getOverWeightFee())
                .overParcelFee(updatedDelivery.getOverParcelFee())
                .isOverWeight(updatedDelivery.isOverWeight())
                .isOverParcel(updatedDelivery.isOverParcel())
                .createdAt(updatedDelivery.getCreatedAt())
                .updatedAt(updatedDelivery.getUpdatedAt())
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public Page<GetAllWaitingDeliveryResponseDto> getAllWaitingDelivery(int page, int size, String sort) {
        Page<GetAllWaitingDeliveryResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<Delivery> deliveries = deliveryRepository.findAllWaitingDelivery(pageable);

        data = deliveries.map(this::toGetAllWaitingDeliveryResponseDto);
        return data;
    }

    @Override
    public ResponseDto<Void> deleteDelivery(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        delivery.setStatus(DeliveryStatus.DELETED);

        deliveryRepository.save(delivery);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    private GetAllDeliveryResponseDto toGetAllDeliveryResponseDto(Delivery delivery) {
        return GetAllDeliveryResponseDto.builder()
                .id(delivery.getId())
                .customerId(delivery.getCustomer().getId())
                .requestDate(delivery.getRequestDate())
                .item(delivery.getItem())
                .weight(delivery.getWeight())
                .message(delivery.getMessage())
                .isHidden(delivery.isHidden())
                .status(delivery.getStatus())
                .pickupName(delivery.getPickupName())
                .recipientName(delivery.getRecipientName())
                .createdAt(delivery.getCreatedAt())
                .updatedAt(delivery.getUpdatedAt())
                .build();
    }

    private GetAllWaitingDeliveryResponseDto toGetAllWaitingDeliveryResponseDto(Delivery delivery) {
        return GetAllWaitingDeliveryResponseDto.builder()
                .id(delivery.getId())
                .customerId(delivery.getCustomer().getId())
                .requestDate(delivery.getRequestDate())
                .item(delivery.getItem())
                .weight(delivery.getWeight())
                .message(delivery.getMessage())
                .isHidden(delivery.isHidden())
                .status(delivery.getStatus())
                .pickupName(delivery.getPickupName())
                .recipientName(delivery.getRecipientName())
                .createdAt(delivery.getCreatedAt())
                .updatedAt(delivery.getUpdatedAt())
                .build();
    }

    // 로그 생성
    private DeliveryUpdateLog buildDeliveryLog(Delivery delivery, User user, String username, String type, String prevData, String newData) {
        return DeliveryUpdateLog.builder()
                .delivery(delivery)
                .user(user)
                .changedByUsername(username)
                .type(type)
                .prevData(String.valueOf(prevData))
                .newData(String.valueOf(newData))
                .build();
    }
}

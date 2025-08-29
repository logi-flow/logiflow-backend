package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.constants.ResponseCode;
import com.logi_flow.backend.common.constants.ResponseMessage;
import com.logi_flow.backend.common.enums.AllocationStatus;
import com.logi_flow.backend.common.enums.ContractStatus;
import com.logi_flow.backend.common.enums.DeliveryStatus;
import com.logi_flow.backend.common.enums.TableRef;
import com.logi_flow.backend.common.enums.user.UserRole;
import com.logi_flow.backend.common.util.DateUtils;
import com.logi_flow.backend.common.util.SortUtils;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.delivery.request.UpdateIsHiddenRequestDto;
import com.logi_flow.backend.dto.delivery.response.GetAllWaitingReturnDeliveryResponseDto;
import com.logi_flow.backend.dto.returnDelivery.request.CreateReturnDeliveryRequestDto;
import com.logi_flow.backend.dto.returnDelivery.request.UpdateReturnDeliveryRequestDto;
import com.logi_flow.backend.dto.returnDelivery.request.UpdateReturnDeliveryStatusRequestDto;
import com.logi_flow.backend.dto.returnDelivery.response.CreateReturnDeliveryResponseDto;
import com.logi_flow.backend.dto.returnDelivery.response.GetAllReturnDeliveryResponseDto;
import com.logi_flow.backend.dto.returnDelivery.response.GetReturnDeliveryDetailResponseDto;
import com.logi_flow.backend.dto.returnDelivery.response.UpdateReturnDeliveryResponseDto;
import com.logi_flow.backend.entity.*;
import com.logi_flow.backend.repository.*;
import com.logi_flow.backend.service.AlertService;
import com.logi_flow.backend.service.DeleteLogService;
import com.logi_flow.backend.service.ReturnDeliveryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReturnDeliveryServiceImpl implements ReturnDeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final ReturnDeliveryRepository returnDeliveryRepository;
    private final ReturnDeliveryUpdateLogRepository returnDeliveryUpdateLogRepository;
    private final ReturnDeliveryStatusLogRepository returnDeliveryStatusLogRepository;
    private final CustomerRepository customerRepository;
    private final ContractRepository contractRepository;
    private final UserRepository userRepository;
    private final DeleteLogService deleteLogService;
    private final DestinationSiteRepository destinationSiteRepository;
    private final RoleRepository roleRepository;
    private final AllocationRepository allocationRepository;

    private final AlertService alertService;

    @Override
    @Transactional
    public ResponseDto<CreateReturnDeliveryResponseDto> createReturnDelivery(Long deliveryId, CreateReturnDeliveryRequestDto dto, UserPrincipal userPrincipal) {
        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Customer customer = customerRepository.findByUser(user).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        if (!customer.getId().equals(delivery.getCustomer().getId())) {
            return ResponseDto.fail(ResponseCode.FORBIDDEN, ResponseMessage.NO_PERMISSION);
        }

        if (returnDeliveryRepository.existsByDeliveryId(delivery.getId())) {
            return ResponseDto.fail(ResponseCode.ALREADY_EXISTS, ResponseMessage.ALREADY_EXISTS);
        }

        Allocation allocation = allocationRepository.findByDeliveryId(delivery.getId()).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        if (!allocation.getStatus().equals(AllocationStatus.IN_PROGRESS) && !allocation.getStatus().equals(AllocationStatus.COMPLETED)) {
            return ResponseDto.fail(ResponseCode.INVALID_STATE, ResponseMessage.INVALID_STATE);
        }

        if (!allocation.getStatus().equals(AllocationStatus.COMPLETED)) {
            return ResponseDto.fail(ResponseCode.DELIVERY_NOT_COMPLETED, ResponseMessage.DELIVERY_NOT_COMPLETED);
        }

        if (LocalDateTime.now().isEqual(allocation.getUpdatedAt().plusDays(7))) {
            return ResponseDto.fail(ResponseCode.ACTION_TOO_LATE, ResponseMessage.ACTION_TOO_LATE);
        }

        Contract contract = contractRepository.findById(delivery.getContract().getId()).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        LocalDate contractEndDate = contract.getEndDate();
        LocalDate today = LocalDate.now();

        if (contractEndDate.isBefore(today)) {
            return ResponseDto.fail(ResponseCode.CONTRACT_EXPIRED, ResponseMessage.CONTRACT_EXPIRED);
        }

        if (!contract.getStatus().equals(ContractStatus.APPROVED)) {
            return ResponseDto.fail(ResponseCode.INVALID_STATE, ResponseMessage.INVALID_STATE);
        }

        DestinationSite destinationSite = destinationSiteRepository.findById(dto.getDestinationSiteId()).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        ReturnDelivery newReturnDelivery = ReturnDelivery.builder()
            .delivery(delivery)
            .requestDate(dto.getRequestDate())
            .reason(dto.getReason())
            .isHidden(false)
            .status(DeliveryStatus.REQUESTED)
            .pickupName(dto.getPickupName())
            .pickupPhone(dto.getPickupPhone())
            .pickupZipcode(dto.getPickupZipcode())
            .pickupAddress(dto.getPickupAddress())
            .pickupAddressDetail(dto.getPickupAddressDetail())
            .destinationSite(destinationSite)
            .finalFee(0)
            .overWeightFee(0)
            .overParcelFee(0)
            .isOverWeight(false)
            .isOverParcel(false)
            .build();

        returnDeliveryRepository.save(newReturnDelivery);

        String alertMessage = "새로운 반품 배송이 등록되었습니다.";

        Role role = roleRepository.findByName(UserRole.ALLOCATIONS_MANAGER).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));
        List<User> allocationManagers = userRepository.findByRoleId(role.getId());

        if (allocationManagers.isEmpty()) {
            return ResponseDto.fail(ResponseCode.USER_NOT_FOUND, ResponseMessage.USER_NOT_FOUND);
        }

        allocationManagers.forEach(allocationManager -> alertService.sendToUser(allocationManager.getId(), alertMessage));

        CreateReturnDeliveryResponseDto responseDto = CreateReturnDeliveryResponseDto.builder()
            .id(newReturnDelivery.getId())
            .customerId(newReturnDelivery.getDelivery().getCustomer().getId())
            .customerName(newReturnDelivery.getDelivery().getCustomer().getName())
            .requestDate(newReturnDelivery.getRequestDate())
            .item(newReturnDelivery.getDelivery().getItem())
            .weight(newReturnDelivery.getDelivery().getWeight())
            .reason(newReturnDelivery.getReason())
            .isHidden(newReturnDelivery.isHidden())
            .status(newReturnDelivery.getStatus())
            .pickupName(newReturnDelivery.getPickupName())
            .pickupPhone(newReturnDelivery.getPickupPhone())
            .pickupZipcode(newReturnDelivery.getPickupZipcode())
            .pickupAddress(newReturnDelivery.getPickupAddress())
            .pickupAddressDetail(newReturnDelivery.getPickupAddressDetail())
            .recipientName(newReturnDelivery.getDestinationSite().getName())
            .recipientPhone(newReturnDelivery.getDestinationSite().getPhoneNumber())
            .recipientZipcode(newReturnDelivery.getDestinationSite().getZipCode())
            .recipientAddress(newReturnDelivery.getDestinationSite().getAddress())
            .recipientAddressDetail(newReturnDelivery.getDestinationSite().getAddressDetail())
            .finalFee(newReturnDelivery.getFinalFee())
            .overWeightFee(newReturnDelivery.getOverWeightFee())
            .overParcelFee(newReturnDelivery.getOverParcelFee())
            .isOverWeight(newReturnDelivery.isOverWeight())
            .isOverParcel(newReturnDelivery.isOverParcel())
            .createdAt(DateUtils.format(newReturnDelivery.getCreatedAt()))
            .updatedAt(DateUtils.format(newReturnDelivery.getUpdatedAt()))
            .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDto);
    }

    @Override
    public Page<GetAllReturnDeliveryResponseDto> getAllReturnDelivery(int page, int size, String sort) {
        Page<GetAllReturnDeliveryResponseDto> responseDtos = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<ReturnDelivery> returnDeliveries = returnDeliveryRepository.findAll(pageable);

        responseDtos = returnDeliveries.map(this::toGetAllDeliveryResponseDto);

        return responseDtos;
    }

    @Override
    public ResponseDto<GetReturnDeliveryDetailResponseDto> getReturnDelivery(Long returnDeliveryId) {
        ReturnDelivery returnDelivery = returnDeliveryRepository.findById(returnDeliveryId).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        GetReturnDeliveryDetailResponseDto responseDto = toGetReturnDeliveryDetailResponseDto(returnDelivery);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDto);
    }

    @Override
    public Page<GetAllReturnDeliveryResponseDto> getMyReturnDeliveries(UserPrincipal userPrincipal, int page, int size, String sort) {
        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Customer customer = customerRepository.findByUser(user).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Page<GetAllReturnDeliveryResponseDto> responseDtos = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<ReturnDelivery> returnDeliveries = returnDeliveryRepository.findByDeliveryCustomerAndIsHiddenFalse(customer, pageable);

        responseDtos = returnDeliveries.map(this::toGetAllDeliveryResponseDto);

        return responseDtos;
    }

    @Override
    @Transactional
    public ResponseDto<UpdateReturnDeliveryResponseDto> updateReturnDelivery(Long returnDeliveryId, UpdateReturnDeliveryRequestDto dto, UserPrincipal userPrincipal) {
        ReturnDelivery returnDelivery = returnDeliveryRepository.findById(returnDeliveryId).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Customer customer = customerRepository.findByUser(user).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        if (!returnDelivery.getDelivery().getCustomer().getId().equals(customer.getId())) {
            return ResponseDto.fail(ResponseCode.FORBIDDEN, ResponseMessage.NO_PERMISSION);
        }

        if (!returnDelivery.getStatus().equals(DeliveryStatus.REQUESTED)) {
            return ResponseDto.fail(ResponseCode.INVALID_REQUESTED_STATE, ResponseMessage.INVALID_REQUESTED_STATE);
        }

        List<ReturnDeliveryUpdateLog> logs = new ArrayList<>();

        updateAndLogAllFields(returnDelivery, dto, user, username, logs);

        ReturnDelivery updateReturnDelivery = returnDeliveryRepository.save(returnDelivery);

        if (!logs.isEmpty()) {
            returnDeliveryUpdateLogRepository.saveAll(logs);
        }

        String alertMessage = "반품 배송 번호 " + returnDelivery.getId() + "가 수정되었습니다.";

        Role role = roleRepository.findByName(UserRole.ALLOCATIONS_MANAGER).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));
        List<User> allocationManagers = userRepository.findByRoleId(role.getId());

        if (allocationManagers.isEmpty()) {
            return ResponseDto.fail(ResponseCode.USER_NOT_FOUND, ResponseMessage.USER_NOT_FOUND);
        }

        allocationManagers.forEach(allocationManager -> alertService.sendToUser(allocationManager.getId(), alertMessage));

        UpdateReturnDeliveryResponseDto responseDto = toUpdateReturnDeliveryResponseDto(updateReturnDelivery);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDto);
    }

    @Override
    @Transactional
    public ResponseDto<UpdateReturnDeliveryResponseDto> updateReturnDeliveryStatus(Long returnDeliveryId, UpdateReturnDeliveryStatusRequestDto dto, UserPrincipal userPrincipal) {
        ReturnDelivery returnDelivery = returnDeliveryRepository.findById(returnDeliveryId).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));
        Contract contract = returnDelivery.getDelivery().getContract();
        Customer customer = returnDelivery.getDelivery().getCustomer();

        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        DeliveryStatus prevStatus = returnDelivery.getStatus();
        DeliveryStatus newStatus = dto.getStatus();

        if (prevStatus == DeliveryStatus.DELETED || newStatus == DeliveryStatus.DELETED) {
            return ResponseDto.fail(ResponseCode.INVALID_STATE, ResponseMessage.INVALID_STATE);
        }

        if (prevStatus == DeliveryStatus.CANCELLED || newStatus == DeliveryStatus.CANCELLED) {
            return ResponseDto.fail(ResponseCode.FORBIDDEN, ResponseMessage.NO_PERMISSION);
        }

        if (prevStatus == newStatus) {
            return ResponseDto.fail(ResponseCode.ALREADY_EXISTS, ResponseMessage.ALREADY_EXISTS);
        }

        switch (prevStatus) {
            case REQUESTED -> {
                if (newStatus == DeliveryStatus.RECEIPTED || newStatus == DeliveryStatus.REJECTED) {
                    returnDelivery.setStatus(newStatus);
                } else {
                    return ResponseDto.fail(ResponseCode.INVALID_STATE, ResponseMessage.INVALID_STATE);
                }
            }
            case RECEIPTED -> {
                if (newStatus == DeliveryStatus.ASSIGNED) {
                    returnDelivery.setStatus(newStatus);
                } else {
                    return ResponseDto.fail(ResponseCode.INVALID_STATE, ResponseMessage.INVALID_STATE);
                }
            }
            default -> {
                return ResponseDto.fail(ResponseCode.INVALID_STATE, ResponseMessage.INVALID_STATE);
            }
        }


        if (returnDelivery.getStatus().equals(DeliveryStatus.RECEIPTED)) {
            int prevOverWeightFee = returnDelivery.getOverWeightFee();
            boolean prevIsOverWeight = returnDelivery.isOverWeight();

            int prevOverParcelFee = returnDelivery.getOverParcelFee();
            boolean prevIsOverParcel = returnDelivery.isOverParcel();

            int prevFinalFee = returnDelivery.getFinalFee();

            int intWeight = returnDelivery.getDelivery().getWeight().intValue();
            int limitKg = contract.getWeightLimitKg();

            if (limitKg < intWeight) {
                int overWeight = intWeight - limitKg;
                int overWeightFee = overWeight * contract.getOverWeightFeePerKg();
                returnDelivery.setOverWeightFee(overWeightFee);

                if (overWeight > 0) {
                    returnDelivery.setOverWeight(true);
                }
            }

            int parcelLimit = contract.getParcelLimit();
            int parcelCount = customer.getParcelCount();

            if (parcelCount >= parcelLimit) {
                returnDelivery.setOverParcelFee(contract.getOverParcelFee());
                returnDelivery.setOverParcel(true);
            } else {
                returnDelivery.setOverParcelFee(0);
                returnDelivery.setOverParcel(false);
            }

            customer.setParcelCount(parcelCount + 1);

            int finalFee = contract.getBaseFee() + returnDelivery.getOverWeightFee() + returnDelivery.getOverParcelFee();
            returnDelivery.setFinalFee(finalFee);


            List<ReturnDeliveryUpdateLog> logs = new ArrayList<>();

            if (prevOverWeightFee != returnDelivery.getOverWeightFee()) {
                logs.add(ReturnDeliveryUpdateLog.builder()
                    .returnDelivery(returnDelivery)
                    .user(user)
                    .changedByUsername(username)
                    .type("over_weight_fee")
                    .prevData(String.valueOf(prevOverWeightFee))
                    .newData(String.valueOf(returnDelivery.getOverWeightFee()))
                    .build());
            }

            if (prevIsOverWeight != returnDelivery.isOverWeight()) {
                logs.add(ReturnDeliveryUpdateLog.builder()
                    .returnDelivery(returnDelivery)
                    .user(user)
                    .changedByUsername(username)
                    .type("is_over_weight")
                    .prevData(String.valueOf(prevIsOverWeight))
                    .newData(String.valueOf(returnDelivery.isOverWeight()))
                    .build());
            }

            if (prevOverParcelFee != returnDelivery.getOverParcelFee()) {
                logs.add(ReturnDeliveryUpdateLog.builder()
                    .returnDelivery(returnDelivery)
                    .user(user)
                    .changedByUsername(username)
                    .type("over_parcel_fee")
                    .prevData(String.valueOf(prevOverParcelFee))
                    .newData(String.valueOf(returnDelivery.getOverParcelFee()))
                    .build());
            }

            if (prevIsOverParcel != returnDelivery.isOverParcel()) {
                logs.add(ReturnDeliveryUpdateLog.builder()
                    .returnDelivery(returnDelivery)
                    .user(user)
                    .changedByUsername(username)
                    .type("is_over_parcel")
                    .prevData(String.valueOf(prevIsOverParcel))
                    .newData(String.valueOf(returnDelivery.isOverParcel()))
                    .build());
            }

            if (prevFinalFee != returnDelivery.getFinalFee()) {
                logs.add(ReturnDeliveryUpdateLog.builder()
                    .returnDelivery(returnDelivery)
                    .user(user)
                    .changedByUsername(username)
                    .type("final_fee")
                    .prevData(String.valueOf(prevFinalFee))
                    .newData(String.valueOf(returnDelivery.getFinalFee()))
                    .build());
            }

            if (!logs.isEmpty()) {
                returnDeliveryUpdateLogRepository.saveAll(logs);
            }
        }

        customerRepository.save(customer);
        ReturnDelivery updatedReturnDelivery = returnDeliveryRepository.save(returnDelivery);

        String statusText = switch (updatedReturnDelivery.getStatus()) {
            case REJECTED -> "거절";
            case ASSIGNED -> "승인";
            case RECEIPTED -> "접수";
            default -> "상태 변경";
        };

        String alertMessage = "반품 배송 번호 " + updatedReturnDelivery.getId() + "가 " + statusText + "되었습니다.";
        alertService.sendToUser(customer.getUser().getId(), alertMessage);

        ReturnDeliveryStatusLog returnDeliveryStatusLog = ReturnDeliveryStatusLog.builder()
            .returnDelivery(returnDelivery)
            .user(user)
            .changedByUsername(username)
            .changeReason(dto.getChangeReason())
            .prevStatus(prevStatus)
            .newStatus(updatedReturnDelivery.getStatus())
            .build();

        returnDeliveryStatusLogRepository.save(returnDeliveryStatusLog);

        UpdateReturnDeliveryResponseDto responseDto = toUpdateReturnDeliveryResponseDto(updatedReturnDelivery);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDto);
    }

    @Override
    @Transactional
    public ResponseDto<Void> deleteReturnDelivery(UserPrincipal userPrincipal, Long returnDeliveryId) {
        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        ReturnDelivery returnDelivery = returnDeliveryRepository.findById(returnDeliveryId).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));
        Customer customer = customerRepository.findById(returnDelivery.getDelivery().getCustomer().getId()).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        if (!returnDelivery.isHidden()) {
            return ResponseDto.fail(ResponseCode.INVALID_STATE, ResponseMessage.INVALID_STATE);
        }

        if (returnDelivery.getStatus() == DeliveryStatus.DELETED) {
            return ResponseDto.fail(ResponseCode.ALREADY_DELETED, ResponseMessage.ALREADY_DELETED);
        }

        returnDelivery.setStatus(DeliveryStatus.DELETED);
        returnDeliveryRepository.save(returnDelivery);

        String alertMessage = "반품 배송 번호 " + returnDelivery.getId() + "가 삭제되었습니다.";
        alertService.sendToUser(customer.getUser().getId(), alertMessage);

        deleteLogService.createLog(TableRef.RETURN_DELIVERY, returnDeliveryId, user);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    @Override
    public Page<GetAllWaitingReturnDeliveryResponseDto> getAllWaitingReturnDelivery(int page, int size, String sort) {
        Page<GetAllWaitingReturnDeliveryResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<ReturnDelivery> returnDeliveries = returnDeliveryRepository.findAllWaitingReturnDelivery(pageable);

        data = returnDeliveries.map(this::toGetAllWaitingDeliveryResponseDto);
        return data;
    }

    @Override
    @Transactional
    public ResponseDto<UpdateReturnDeliveryResponseDto> updateReturnDeliveryStatusCancel(Long returnDeliveryId, UpdateReturnDeliveryStatusRequestDto dto, UserPrincipal userPrincipal) {
        ReturnDelivery returnDelivery = returnDeliveryRepository.findById(returnDeliveryId).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));
        Customer customer = returnDelivery.getDelivery().getCustomer();

        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        if (!customer.getUser().getId().equals(user.getId())) {
            return ResponseDto.fail("FORBIDDEN", ResponseMessage.NO_PERMISSION);
        }

        DeliveryStatus prevStatus = returnDelivery.getStatus();

        if (prevStatus.equals(DeliveryStatus.REQUESTED)) {
            if (dto.getStatus() == DeliveryStatus.CANCELLED) {
                returnDelivery.setStatus(dto.getStatus());
            } else {
                return ResponseDto.fail(ResponseCode.INVALID_STATE, ResponseMessage.INVALID_STATE);
            }
        } else {
            return ResponseDto.fail(ResponseCode.INVALID_STATE, ResponseMessage.INVALID_STATE);
        }

        ReturnDelivery updatedReturnDelivery = returnDeliveryRepository.save(returnDelivery);

        String alertMessage = "반품 배송 번호 " + updatedReturnDelivery.getId() + "가 신청 취소되었습니다.";
        Role role = roleRepository.findByName(UserRole.ALLOCATIONS_MANAGER).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));
        List<User> allocationManagers = userRepository.findByRoleId(role.getId());

        if (allocationManagers.isEmpty()) {
            return ResponseDto.fail(ResponseCode.USER_NOT_FOUND, ResponseMessage.USER_NOT_FOUND);
        }

        allocationManagers.forEach(allocationManager -> alertService.sendToUser(allocationManager.getId(), alertMessage));

        ReturnDeliveryStatusLog returnDeliveryStatusLog = ReturnDeliveryStatusLog.builder()
            .returnDelivery(returnDelivery)
            .user(user)
            .changedByUsername(username)
            .changeReason(dto.getChangeReason())
            .prevStatus(prevStatus)
            .newStatus(updatedReturnDelivery.getStatus())
            .build();

        returnDeliveryStatusLogRepository.save(returnDeliveryStatusLog);

        UpdateReturnDeliveryResponseDto responseDto = toUpdateReturnDeliveryResponseDto(updatedReturnDelivery);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDto);
    }

    @Override
    @Transactional
    public ResponseDto<UpdateReturnDeliveryResponseDto> updateReturnDeliveryIsHidden(Long returnDeliveryId, UpdateIsHiddenRequestDto dto, UserPrincipal userPrincipal) {
        ReturnDelivery returnDelivery = returnDeliveryRepository.findById(returnDeliveryId).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Customer customer = customerRepository.findByUser(user).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        if (!returnDelivery.getDelivery().getCustomer().getId().equals(customer.getId())) {
            return ResponseDto.fail("FORBIDDEN", ResponseMessage.NO_PERMISSION);
        }

        Allocation allocation = allocationRepository.findByReturnDeliveryId(returnDelivery.getId()).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        if (allocation.getStatus().equals(AllocationStatus.COMPLETED)) {
            return ResponseDto.fail(ResponseCode.DELIVERY_NOT_COMPLETED, ResponseMessage.DELIVERY_NOT_COMPLETED);
        }

        if (!LocalDateTime.now().isAfter(allocation.getUpdatedAt().plusDays(7))) {
            return ResponseDto.fail(ResponseCode.ACTION_TOO_EARLY, ResponseMessage.ACTION_TOO_EARLY);
        }

        boolean prevIsHidden = returnDelivery.isHidden();

        if (dto.getIsHidden() != null && dto.getIsHidden() != returnDelivery.isHidden()) {
            returnDelivery.setHidden(dto.getIsHidden());
        }

        ReturnDelivery updatedReturnDelivery = returnDeliveryRepository.save(returnDelivery);

        ReturnDeliveryUpdateLog returnDeliveryUpdateLog = ReturnDeliveryUpdateLog.builder()
            .returnDelivery(returnDelivery)
            .user(user)
            .changedByUsername(username)
            .type("is_hidden")
            .prevData(String.valueOf(prevIsHidden))
            .newData(String.valueOf(returnDelivery.isHidden()))
            .build();

        returnDeliveryUpdateLogRepository.save(returnDeliveryUpdateLog);

        Role role = roleRepository.findByName(UserRole.ALLOCATIONS_MANAGER).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));
        List<User> allocationsManager = userRepository.findByRoleId(role.getId());
        String alertMessage = "반품 번호 " + returnDelivery.getId() + "가 숨김 처리되었습니다.";
        if (allocationsManager.isEmpty()) {
            return ResponseDto.fail(ResponseCode.USER_NOT_FOUND, ResponseMessage.USER_NOT_FOUND);
        }

        allocationsManager.forEach(allocationManager -> alertService.sendToUser(allocationManager.getId(), alertMessage));

        UpdateReturnDeliveryResponseDto data = toUpdateReturnDeliveryResponseDto(updatedReturnDelivery);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    private GetAllReturnDeliveryResponseDto toGetAllDeliveryResponseDto(ReturnDelivery returnDelivery) {
        return GetAllReturnDeliveryResponseDto.builder()
            .id(returnDelivery.getId())
            .customerId(returnDelivery.getDelivery().getCustomer().getId())
            .customerName(returnDelivery.getDelivery().getCustomer().getName())
            .requestDate(returnDelivery.getRequestDate())
            .item(returnDelivery.getDelivery().getItem())
            .weight(returnDelivery.getDelivery().getWeight())
            .reason(returnDelivery.getReason())
            .status(returnDelivery.getStatus())
            .pickupName(returnDelivery.getPickupName())
            .recipientName(returnDelivery.getDestinationSite().getName())
            .createdAt(DateUtils.format(returnDelivery.getCreatedAt()))
            .updatedAt(DateUtils.format(returnDelivery.getUpdatedAt()))
            .build();
    }

    private void updateAndLogAllFields(ReturnDelivery returnDelivery, UpdateReturnDeliveryRequestDto dto, User user, String username, List<ReturnDeliveryUpdateLog> logs) {
        Arrays.stream(dto.getClass().getDeclaredFields()).forEach(field -> {
            field.setAccessible(true);

            try {
                String fieldName = field.getName();

                if ("destinationSiteId".equals(fieldName)) {
                    Long newSiteId = dto.getDestinationSiteId();
                    Long oldSiteId = returnDelivery.getDestinationSite().getId();

                    if (newSiteId != null && !newSiteId.equals(oldSiteId)) {
                        logs.add(buildReturnDeliveryLog(
                            returnDelivery,
                            user,
                            username,
                            "destinationSiteId",
                            String.valueOf(oldSiteId),
                            String.valueOf(newSiteId)
                        ));

                        DestinationSite newSite = destinationSiteRepository.findById(newSiteId).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

                        returnDelivery.setDestinationSite(newSite);
                    }
                    return;
                }
                String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Method getter = dto.getClass().getMethod(getterName);
                Object newValue = getter.invoke(dto);

                if (newValue != null && !Objects.equals(newValue, returnDelivery.getClass().getMethod(getterName).invoke(returnDelivery))) {
                    String setterName = "set" + getterName.substring(3);
                    Method setter = returnDelivery.getClass().getMethod(setterName, field.getType());

                    logs.add(buildReturnDeliveryLog(returnDelivery, user, username, fieldName, String.valueOf(returnDelivery.getClass().getMethod(getterName).invoke(returnDelivery)), String.valueOf(newValue)));

                    setter.invoke(returnDelivery, newValue);
                }
            } catch (Exception e) {
                System.out.println("리플렉션 처리 중 오류 발생: " + e.getMessage());
            }
        });
    }

    private ReturnDeliveryUpdateLog buildReturnDeliveryLog(ReturnDelivery returnDelivery, User user, String username, String fieldName, String oldValue, String newValue) {
        return ReturnDeliveryUpdateLog.builder()
            .returnDelivery(returnDelivery)
            .user(user)
            .changedByUsername(username)
            .type(fieldName)
            .prevData(oldValue)
            .newData(newValue)
            .build();
    }

    private GetAllWaitingReturnDeliveryResponseDto toGetAllWaitingDeliveryResponseDto(ReturnDelivery returnDelivery) {
        return GetAllWaitingReturnDeliveryResponseDto.builder()
            .id(returnDelivery.getId())
            .customerId(returnDelivery.getDelivery().getCustomer().getId())
            .requestDate(returnDelivery.getRequestDate())
            .item(returnDelivery.getDelivery().getItem())
            .weight(returnDelivery.getDelivery().getWeight())
            .reason(returnDelivery.getReason())
            .status(returnDelivery.getStatus())
            .pickupName(returnDelivery.getPickupName())
            .recipientName(returnDelivery.getDestinationSite().getName())
            .createdAt(DateUtils.format(returnDelivery.getCreatedAt()))
            .updatedAt(DateUtils.format(returnDelivery.getUpdatedAt()))
            .build();
    }

    private UpdateReturnDeliveryResponseDto toUpdateReturnDeliveryResponseDto(ReturnDelivery updatedReturnDelivery) {
        return UpdateReturnDeliveryResponseDto.builder()
            .id(updatedReturnDelivery.getId())
            .customerId(updatedReturnDelivery.getDelivery().getCustomer().getId())
            .customerName(updatedReturnDelivery.getDelivery().getCustomer().getName())
            .requestDate(updatedReturnDelivery.getRequestDate())
            .item(updatedReturnDelivery.getDelivery().getItem())
            .weight(updatedReturnDelivery.getDelivery().getWeight())
            .reason(updatedReturnDelivery.getReason())
            .isHidden(updatedReturnDelivery.isHidden())
            .status(updatedReturnDelivery.getStatus())
            .pickupName(updatedReturnDelivery.getPickupName())
            .pickupPhone(updatedReturnDelivery.getPickupPhone())
            .pickupZipcode(updatedReturnDelivery.getPickupZipcode())
            .pickupAddress(updatedReturnDelivery.getPickupAddress())
            .pickupAddressDetail(updatedReturnDelivery.getPickupAddressDetail())
            .recipientName(updatedReturnDelivery.getDestinationSite().getName())
            .recipientPhone(updatedReturnDelivery.getDestinationSite().getPhoneNumber())
            .recipientZipcode(updatedReturnDelivery.getDestinationSite().getZipCode())
            .recipientAddress(updatedReturnDelivery.getDestinationSite().getAddress())
            .recipientAddressDetail(updatedReturnDelivery.getDestinationSite().getAddressDetail())
            .finalFee(updatedReturnDelivery.getFinalFee())
            .overWeightFee(updatedReturnDelivery.getOverWeightFee())
            .overParcelFee(updatedReturnDelivery.getOverParcelFee())
            .isOverWeight(updatedReturnDelivery.isOverWeight())
            .isOverParcel(updatedReturnDelivery.isOverParcel())
            .createdAt(DateUtils.format(updatedReturnDelivery.getCreatedAt()))
            .updatedAt(DateUtils.format(updatedReturnDelivery.getUpdatedAt()))
            .build();
    }

    private GetReturnDeliveryDetailResponseDto toGetReturnDeliveryDetailResponseDto(ReturnDelivery returnDelivery) {
        return GetReturnDeliveryDetailResponseDto.builder()
            .id(returnDelivery.getId())
            .customerId(returnDelivery.getDelivery().getCustomer().getId())
            .customerName(returnDelivery.getDelivery().getCustomer().getName())
            .requestDate(returnDelivery.getRequestDate())
            .item(returnDelivery.getDelivery().getItem())
            .weight(returnDelivery.getDelivery().getWeight())
            .reason(returnDelivery.getReason())
            .status(returnDelivery.getStatus())
            .pickupName(returnDelivery.getPickupName())
            .pickupPhone(returnDelivery.getPickupPhone())
            .pickupZipcode(returnDelivery.getPickupZipcode())
            .pickupAddress(returnDelivery.getPickupAddress())
            .pickupAddressDetail(returnDelivery.getPickupAddressDetail())
            .recipientName(returnDelivery.getDestinationSite().getName())
            .recipientPhone(returnDelivery.getDestinationSite().getPhoneNumber())
            .recipientZipcode(returnDelivery.getDestinationSite().getZipCode())
            .recipientAddress(returnDelivery.getDestinationSite().getAddress())
            .recipientAddressDetail(returnDelivery.getDestinationSite().getAddressDetail())
            .finalFee(returnDelivery.getFinalFee())
            .overWeightFee(returnDelivery.getOverWeightFee())
            .overParcelFee(returnDelivery.getOverParcelFee())
            .isOverWeight(returnDelivery.isOverWeight())
            .isOverParcel(returnDelivery.isOverParcel())
            .createdAt(DateUtils.format(returnDelivery.getCreatedAt()))
            .updatedAt(DateUtils.format(returnDelivery.getUpdatedAt()))
            .build();
    }
}

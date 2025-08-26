package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.constants.ResponseCode;
import com.logi_flow.backend.common.constants.ResponseMessage;
import com.logi_flow.backend.common.enums.ContractStatus;
import com.logi_flow.backend.common.enums.DeliveryStatus;
import com.logi_flow.backend.common.enums.TableRef;
import com.logi_flow.backend.common.enums.user.UserRole;
import com.logi_flow.backend.common.util.DateUtils;
import com.logi_flow.backend.common.util.SortUtils;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
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
import com.logi_flow.backend.service.DeleteLogService;
import com.logi_flow.backend.service.ReturnDeliveryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.time.LocalDate;
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

    @Override
    @Transactional
    public ResponseDto<CreateReturnDeliveryResponseDto> createReturnDelivery(Long deliveryId, CreateReturnDeliveryRequestDto dto, UserPrincipal userPrincipal) {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        if (returnDeliveryRepository.existsByDeliveryId(delivery.getId())) {
            throw new IllegalArgumentException("이미 반품 신청이 되어있습니다.");
        }

        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Customer customer = customerRepository.findByUser(user).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        if (!customer.getId().equals(delivery.getCustomer().getId())) {
            throw new AccessDeniedException("해당 배송에 대한 접근 권한이 없습니다.");
        }

        Contract contract = contractRepository.findById(delivery.getContract().getId()).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        LocalDate contractEndDate = contract.getEndDate();
        LocalDate today = LocalDate.now();

        if (contractEndDate.isBefore(today)) {
            throw new IllegalArgumentException("계약 종료일이 지남");
        }

        if (!contract.getStatus().equals(ContractStatus.APPROVED)) {
            throw new IllegalArgumentException("계약 상태가 승인이 아님");
        }

        ReturnDelivery newReturnDelivery = ReturnDelivery.builder()
            .delivery(delivery)
            .requestDate(dto.getRequestDate())
            .reason(dto.getReason())
            .status(DeliveryStatus.REQUESTED)
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

        returnDeliveryRepository.save(newReturnDelivery);

        CreateReturnDeliveryResponseDto responseDto = CreateReturnDeliveryResponseDto.builder()
            .id(newReturnDelivery.getId())
            .customerId(newReturnDelivery.getDelivery().getCustomer().getId())
            .customerName(newReturnDelivery.getDelivery().getCustomer().getName())
            .requestDate(newReturnDelivery.getRequestDate())
            .item(newReturnDelivery.getDelivery().getItem())
            .weight(newReturnDelivery.getDelivery().getWeight())
            .reason(newReturnDelivery.getReason())
            .status(newReturnDelivery.getStatus())
            .pickupName(newReturnDelivery.getPickupName())
            .pickupPhone(newReturnDelivery.getPickupPhone())
            .pickupZipcode(newReturnDelivery.getPickupZipCode())
            .pickupAddress(newReturnDelivery.getPickupAddress())
            .pickupAddressDetail(newReturnDelivery.getPickupAddressDetail())
            .recipientName(newReturnDelivery.getRecipientName())
            .recipientPhone(newReturnDelivery.getRecipientPhone())
            .recipientZipcode(newReturnDelivery.getRecipientZipcode())
            .recipientAddress(newReturnDelivery.getRecipientAddress())
            .recipientAddressDetail(newReturnDelivery.getRecipientAddressDetail())
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

        GetReturnDeliveryDetailResponseDto responseDto = GetReturnDeliveryDetailResponseDto.builder()
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
            .pickupZipcode(returnDelivery.getPickupZipCode())
            .pickupAddress(returnDelivery.getPickupAddress())
            .pickupAddressDetail(returnDelivery.getPickupAddressDetail())
            .recipientName(returnDelivery.getRecipientName())
            .recipientPhone(returnDelivery.getRecipientPhone())
            .recipientZipcode(returnDelivery.getRecipientZipcode())
            .recipientAddress(returnDelivery.getRecipientAddress())
            .recipientAddressDetail(returnDelivery.getRecipientAddressDetail())
            .finalFee(returnDelivery.getFinalFee())
            .overWeightFee(returnDelivery.getOverWeightFee())
            .overParcelFee(returnDelivery.getOverParcelFee())
            .isOverWeight(returnDelivery.isOverWeight())
            .isOverParcel(returnDelivery.isOverParcel())
            .createdAt(DateUtils.format(returnDelivery.getCreatedAt()))
            .updatedAt(DateUtils.format(returnDelivery.getUpdatedAt()))
            .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDto);
    }

    @Override
    public Page<GetAllReturnDeliveryResponseDto> getMyReturnDeliveries(UserPrincipal userPrincipal, int page, int size, String sort) {
        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Customer customer = customerRepository.findByUser(user).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Page<GetAllReturnDeliveryResponseDto> responseDtos = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<ReturnDelivery> returnDeliveries = returnDeliveryRepository.findByDeliveryCustomer(customer, pageable);

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
            return ResponseDto.fail("FORBIDDEN", ResponseMessage.NO_PERMISSION);
        }

        if (!returnDelivery.getStatus().equals(DeliveryStatus.REQUESTED)) {
            return ResponseDto.fail(ResponseCode.FAILED, "배송 요청 상태에만 수정 가능");
        }

        List<ReturnDeliveryUpdateLog> logs = new ArrayList<>();

        updateAndLogAllFields(returnDelivery, dto, user, username, logs);

        ReturnDelivery updateDelivery = returnDeliveryRepository.save(returnDelivery);

        if (!logs.isEmpty()) {
            returnDeliveryUpdateLogRepository.saveAll(logs);
        }

        UpdateReturnDeliveryResponseDto responseDto = UpdateReturnDeliveryResponseDto.builder()
            .id(updateDelivery.getId())
            .customerId(updateDelivery.getDelivery().getCustomer().getId())
            .requestDate(updateDelivery.getRequestDate())
            .item(updateDelivery.getDelivery().getItem())
            .weight(updateDelivery.getDelivery().getWeight())
            .reason(updateDelivery.getReason())
            .status(updateDelivery.getStatus())
            .pickupName(updateDelivery.getPickupName())
            .pickupPhone(updateDelivery.getPickupPhone())
            .pickupZipcode(updateDelivery.getPickupZipCode())
            .pickupAddress(updateDelivery.getPickupAddress())
            .pickupAddressDetail(updateDelivery.getPickupAddressDetail())
            .recipientName(updateDelivery.getRecipientName())
            .recipientPhone(updateDelivery.getRecipientPhone())
            .recipientZipcode(updateDelivery.getRecipientZipcode())
            .recipientAddress(updateDelivery.getRecipientAddress())
            .recipientAddressDetail(updateDelivery.getRecipientAddressDetail())
            .finalFee(updateDelivery.getFinalFee())
            .overWeightFee(updateDelivery.getOverWeightFee())
            .overParcelFee(updateDelivery.getOverParcelFee())
            .isOverWeight(updateDelivery.isOverWeight())
            .isOverParcel(updateDelivery.isOverParcel())
            .createdAt(DateUtils.format(updateDelivery.getCreatedAt()))
            .updatedAt(DateUtils.format(updateDelivery.getUpdatedAt()))
            .build();

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

        if (!user.getRole().getName().equals(UserRole.ADMIN)) {
            return ResponseDto.fail("FORBIDDEN", ResponseMessage.NO_PERMISSION);
        }

        DeliveryStatus prevStatus = returnDelivery.getStatus();

        if (dto.getStatus() != returnDelivery.getStatus()) {
            returnDelivery.setStatus(dto.getStatus());
        } else  {
            return ResponseDto.fail(ResponseCode.ALREADY_EXISTS, ResponseMessage.ALREADY_EXISTS);
        }

        if (returnDelivery.getStatus().equals(DeliveryStatus.ASSIGNED)) {
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
            int parcelCount = returnDelivery.getDelivery().getCustomer().getParcelCount();


            if (parcelCount >= parcelLimit) {
                returnDelivery.setOverParcelFee(contract.getOverParcelFee());
                returnDelivery.setOverParcel(true);
                customer.setParcelCount(parcelCount + 1);
            } else {
                returnDelivery.setOverParcelFee(0);
                returnDelivery.setOverParcel(false);
                customer.setParcelCount(parcelCount + 1);
            }

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
                    .newData(String.valueOf(!prevIsOverWeight))
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
                    .newData(String.valueOf(!prevIsOverParcel))
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

        ReturnDeliveryStatusLog returnDeliveryStatusLog = ReturnDeliveryStatusLog.builder()
            .returnDelivery(returnDelivery)
            .user(user)
            .changedByUsername(username)
            .changeReason(dto.getChangeReason())
            .prevStatus(prevStatus)
            .newStatus(updatedReturnDelivery.getStatus())
            .build();

        returnDeliveryStatusLogRepository.save(returnDeliveryStatusLog);

        UpdateReturnDeliveryResponseDto responseDto = UpdateReturnDeliveryResponseDto.builder()
            .id(updatedReturnDelivery.getId())
            .customerId(updatedReturnDelivery.getDelivery().getCustomer().getId())
            .customerName(updatedReturnDelivery.getDelivery().getCustomer().getName())
            .requestDate(updatedReturnDelivery.getRequestDate())
            .item(updatedReturnDelivery.getDelivery().getItem())
            .weight(updatedReturnDelivery.getDelivery().getWeight())
            .reason(updatedReturnDelivery.getReason())
            .status(updatedReturnDelivery.getStatus())
            .pickupName(updatedReturnDelivery.getPickupName())
            .pickupPhone(updatedReturnDelivery.getPickupPhone())
            .pickupZipcode(updatedReturnDelivery.getPickupZipCode())
            .pickupAddress(updatedReturnDelivery.getPickupAddress())
            .pickupAddressDetail(updatedReturnDelivery.getPickupAddressDetail())
            .recipientName(updatedReturnDelivery.getRecipientName())
            .recipientPhone(updatedReturnDelivery.getRecipientPhone())
            .recipientZipcode(updatedReturnDelivery.getRecipientZipcode())
            .recipientAddress(updatedReturnDelivery.getRecipientAddress())
            .recipientAddressDetail(updatedReturnDelivery.getRecipientAddressDetail())
            .finalFee(updatedReturnDelivery.getFinalFee())
            .overWeightFee(updatedReturnDelivery.getOverWeightFee())
            .overParcelFee(updatedReturnDelivery.getOverParcelFee())
            .isOverWeight(updatedReturnDelivery.isOverWeight())
            .isOverParcel(updatedReturnDelivery.isOverParcel())
            .createdAt(DateUtils.format(updatedReturnDelivery.getCreatedAt()))
            .updatedAt(DateUtils.format(updatedReturnDelivery.getUpdatedAt()))
            .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDto);
    }

    @Override
    @Transactional
    public ResponseDto<Void> deleteReturnDelivery(UserPrincipal userPrincipal, Long returnDeliveryId) {
        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        if (!user.getRole().getName().equals(UserRole.ADMIN)) {
            return ResponseDto.fail("FORBIDDEN", ResponseMessage.NO_PERMISSION);
        }

        ReturnDelivery returnDelivery = returnDeliveryRepository.findById(returnDeliveryId).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        if (returnDelivery.getStatus() == DeliveryStatus.DELETED) {
            return ResponseDto.fail(ResponseCode.ALREADY_DELETED, ResponseMessage.ALREADY_DELETED);
        }

        returnDelivery.setStatus(DeliveryStatus.DELETED);
        returnDeliveryRepository.save(returnDelivery);

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
            .recipientName(returnDelivery.getRecipientName())
            .createdAt(DateUtils.format(returnDelivery.getCreatedAt()))
            .updatedAt(DateUtils.format(returnDelivery.getUpdatedAt()))
            .build();
    }

    private void updateAndLogAllFields(ReturnDelivery returnDelivery, UpdateReturnDeliveryRequestDto dto, User user, String username, List<ReturnDeliveryUpdateLog> logs) {
        Arrays.stream(dto.getClass().getDeclaredFields()).forEach(field -> {
            field.setAccessible(true);

            try {
                String getterName = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                Method getter = dto.getClass().getMethod(getterName);
                Object newValue = getter.invoke(dto);

                if (newValue != null && !Objects.equals(newValue, returnDelivery.getClass().getMethod(getterName).invoke(returnDelivery))) {
                    String setterName = "set" + getterName.substring(3);
                    Method setter = returnDelivery.getClass().getMethod(setterName, field.getType());

                    logs.add(buildReturnDeliveryLog(returnDelivery, user, username, field.getName(), String.valueOf(returnDelivery.getClass().getMethod(getterName).invoke(returnDelivery)), String.valueOf(newValue)));

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
            .recipientName(returnDelivery.getRecipientName())
            .createdAt(DateUtils.format(returnDelivery.getCreatedAt()))
            .updatedAt(DateUtils.format(returnDelivery.getUpdatedAt()))
            .build();
    }
}

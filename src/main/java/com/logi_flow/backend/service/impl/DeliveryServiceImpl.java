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
import com.logi_flow.backend.dto.delivery.request.CreateDeliveryRequestDto;
import com.logi_flow.backend.dto.delivery.request.UpdateDeliveryRequestDto;
import com.logi_flow.backend.dto.delivery.request.UpdateDeliveryStatusRequestDto;
import com.logi_flow.backend.dto.delivery.request.UpdateIsHiddenRequestDto;
import com.logi_flow.backend.dto.delivery.response.*;
import com.logi_flow.backend.entity.*;
import com.logi_flow.backend.repository.*;
import com.logi_flow.backend.service.AlertService;
import com.logi_flow.backend.service.DeleteLogService;
import com.logi_flow.backend.service.DeliveryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final ContractRepository contractRepository;
    private final DeliveryRepository deliveryRepository;

    private final DeliveryUpdateLogRepository deliveryUpdateLogRepository;
    private final DeliveryStatusLogRepository deliveryStatusLogRepository;
    private final DeleteLogService deleteLogService;

    private final AlertService alertService;

    @Override
    @Transactional
    public ResponseDto<CreateDeliveryResponseDto> createDelivery(CreateDeliveryRequestDto dto, UserPrincipal userPrincipal) {
        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Customer customer = customerRepository.findByUser(user).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Contract contract = contractRepository.findById(dto.getContractId()).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        LocalDate contractEndDate = contract.getEndDate();
        LocalDate today = LocalDate.now();

        if(contractEndDate.isBefore(today)) {
            throw new IllegalArgumentException("계약 종료일이 지남");
        }

        if(!contract.getStatus().equals(ContractStatus.APPROVED)) {
            throw new IllegalArgumentException("계약 상태가 승인이 아님");
        }

        Delivery newDelivery = Delivery.builder()
                .contract(contract)
                .customer(customer)
                .requestDate(dto.getRequestDate())
                .item(dto.getItem())
                .weight(dto.getWeight())
                .message(dto.getMessage())
                .isHidden(false)
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
                .createdAt(DateUtils.format(newDelivery.getCreatedAt()))
                .updatedAt(DateUtils.format(newDelivery.getUpdatedAt()))
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
                .createdAt(DateUtils.format(delivery.getCreatedAt()))
                .updatedAt(DateUtils.format(delivery.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public Page<GetAllDeliveryResponseDto> getMyDeliveries(UserPrincipal userPrincipal, int page, int size, String sort) {
        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Customer customer = customerRepository.findByUser(user).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Page<GetAllDeliveryResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<Delivery> deliveries = deliveryRepository.findByCustomerId(customer.getId(), pageable);

        data = deliveries.map(this::toGetAllDeliveryResponseDto);

        return data;
    }

    @Override
    @Transactional
    public ResponseDto<UpdateDeliveryResponseDto> updateDeliveryIsHidden(Long deliveryId, UpdateIsHiddenRequestDto dto, UserPrincipal userPrincipal) {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Customer customer = customerRepository.findByUser(user).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        if(!delivery.getCustomer().getId().equals(customer.getId())) {
            return ResponseDto.fail("FORBIDDEN", ResponseMessage.NO_PERMISSION);
        }

        boolean prevIsHidden = delivery.isHidden();

        if(dto.getIsHidden() != null && !dto.getIsHidden().equals(delivery.isHidden())) {
            delivery.setHidden(dto.getIsHidden());
        }

        Delivery updatedDelivery = deliveryRepository.save(delivery);

        DeliveryUpdateLog deliveryUpdateLog = DeliveryUpdateLog.builder()
                .delivery(delivery)
                .user(user)
                .changedByUsername(username)
                .type("is_hidden")
                .prevData(prevIsHidden ? "true" : "false")
                .newData(updatedDelivery.isHidden() ? "true" : "false")
                .build();

        deliveryUpdateLogRepository.save(deliveryUpdateLog);


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
                .createdAt(DateUtils.format(updatedDelivery.getCreatedAt()))
                .updatedAt(DateUtils.format(updatedDelivery.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    @Transactional
    public ResponseDto<UpdateDeliveryResponseDto> updateDelivery(Long deliveryId, UpdateDeliveryRequestDto dto, UserPrincipal userPrincipal) {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Customer customer = customerRepository.findByUser(user).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        if(!delivery.getCustomer().getId().equals(customer.getId())) {
            return ResponseDto.fail("FORBIDDEN", ResponseMessage.NO_PERMISSION);
        }

        if(!delivery.getStatus().equals(DeliveryStatus.REQUESTED)) {
            return ResponseDto.fail(ResponseCode.FAILED, "배송 요청 상태에만 수정 가능");
        }

        List<DeliveryUpdateLog> logs = new ArrayList<>();

        if(!Objects.equals(dto.getRequestDate(), delivery.getRequestDate())) {
            logs.add(buildDeliveryLog(delivery, user, username, "request_date", String.valueOf(delivery.getRequestDate()), String.valueOf(dto.getRequestDate())));
            delivery.setRequestDate(dto.getRequestDate());
        }

        if(!Objects.equals(dto.getItem(), delivery.getItem())) {
            logs.add(buildDeliveryLog(delivery, user, username, "item", delivery.getItem(), dto.getItem()));
            delivery.setItem(dto.getItem());
        }

        if(dto.getWeight() != null) {
            if(delivery.getWeight() == null || dto.getWeight().compareTo(delivery.getWeight()) != 0) {
                logs.add(buildDeliveryLog(delivery, user, username, "weight", delivery.getWeight() != null ? delivery.getWeight().toString() : null, dto.getWeight().toString()));
                delivery.setWeight(dto.getWeight());
            }
        }

        if(!Objects.equals(dto.getMessage(), delivery.getMessage())) {
            logs.add(buildDeliveryLog(delivery, user, username, "message", delivery.getMessage(), dto.getMessage()));
            delivery.setMessage(dto.getMessage());
        }

        if(!Objects.equals(dto.getPickupName(), delivery.getPickupName())) {
            logs.add(buildDeliveryLog(delivery, user, username, "pickup_name", delivery.getPickupName(), dto.getPickupName()));
            delivery.setPickupName(dto.getPickupName());
        }

        if(!Objects.equals(dto.getPickupPhone(), delivery.getPickupPhone())) {
            logs.add(buildDeliveryLog(delivery, user, username, "pickup_phone", delivery.getPickupPhone(), dto.getPickupPhone()));
            delivery.setPickupPhone(dto.getPickupPhone());
        }

        if(!Objects.equals(dto.getPickupZipcode(), delivery.getPickupZipCode())) {
            logs.add(buildDeliveryLog(delivery, user, username, "pickup_zipcode", delivery.getPickupZipCode(), dto.getPickupZipcode()));
            delivery.setPickupZipCode(dto.getPickupZipcode());
        }

        if(!Objects.equals(dto.getPickupAddress(), delivery.getPickupAddress())) {
            logs.add(buildDeliveryLog(delivery, user, username, "pickup_address", delivery.getPickupAddress(), dto.getPickupAddress()));
            delivery.setPickupAddress(dto.getPickupAddress());
        }

        if(dto.getPickupAddressDetail() != null && !Objects.equals(dto.getPickupAddressDetail(), delivery.getPickupAddressDetail())) {
            logs.add(buildDeliveryLog(delivery, user, username, "pickup_address_detail", dto.getPickupAddressDetail(), dto.getPickupAddressDetail()));
            delivery.setPickupAddressDetail(dto.getPickupAddressDetail());
        }

        if(!Objects.equals(dto.getRecipientName(), delivery.getRecipientName())) {
            logs.add(buildDeliveryLog(delivery, user, username, "recipient_name", delivery.getRecipientName(), dto.getRecipientName()));
            delivery.setRecipientName(dto.getRecipientName());
        }

        if(!Objects.equals(dto.getRecipientPhone(), delivery.getRecipientPhone())) {
            logs.add(buildDeliveryLog(delivery, user, username, "recipient_phone", delivery.getRecipientPhone(), dto.getRecipientPhone()));
            delivery.setRecipientPhone(dto.getRecipientPhone());
        }

        if(!Objects.equals(dto.getRecipientZipcode(), delivery.getRecipientZipcode())) {
            logs.add(buildDeliveryLog(delivery, user, username, "recipient_zipcode", delivery.getRecipientZipcode(), dto.getRecipientZipcode()));
            delivery.setPickupZipCode(dto.getRecipientZipcode());
        }

        if(!Objects.equals(dto.getRecipientAddress(), delivery.getRecipientAddress())) {
            logs.add(buildDeliveryLog(delivery, user, username, "recipient_address", delivery.getRecipientAddress(), dto.getRecipientAddress()));
            delivery.setRecipientAddress(dto.getRecipientAddress());
        }

        if(dto.getRecipientAddressDetail() != null && !Objects.equals(dto.getRecipientAddressDetail(), delivery.getRecipientAddressDetail())) {
            logs.add(buildDeliveryLog(delivery, user, username, "recipient_address_detail", dto.getRecipientAddressDetail(), dto.getRecipientAddressDetail()));
            delivery.setPickupAddressDetail(dto.getRecipientAddressDetail());
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
                .createdAt(DateUtils.format(updatedDelivery.getCreatedAt()))
                .updatedAt(DateUtils.format(updatedDelivery.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    @Transactional
    public ResponseDto<UpdateDeliveryResponseDto> updateDeliveryStatus(Long deliveryId, UpdateDeliveryStatusRequestDto dto, UserPrincipal userPrincipal) {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));
        Contract contract = delivery.getContract();

        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        if(!user.getRole().getName().equals(UserRole.ADMIN)) {
            return ResponseDto.fail("FORBIDDEN", ResponseMessage.NO_PERMISSION);
        }

        DeliveryStatus prevStatus = delivery.getStatus();

        if(dto.getStatus() != delivery.getStatus()) {
            delivery.setStatus(dto.getStatus());
        }

        if(delivery.getStatus().equals(DeliveryStatus.ASSIGNED)) {

            int prevOverWeightFee = delivery.getOverWeightFee();
            boolean prevIsOverWeight = delivery.isOverWeight();

            int prevOverParcelFee = delivery.getOverParcelFee();
            boolean prevIsOverParcel = delivery.isOverParcel();

            int prevFinalFee = delivery.getFinalFee();

            int intWeight = delivery.getWeight().intValue();
            int limitKg = contract.getWeightLimitKg();
            if(limitKg < intWeight) {
                int overWeight = intWeight - limitKg;
                int overWeightFee = overWeight * contract.getOverWeightFeePerKg();
                delivery.setOverWeightFee(overWeightFee);

                if (overWeight > 0) {
                    delivery.setOverWeight(true);
                }
            }

            int parcelLimit = contract.getParcelLimit();
            int parcelCount = delivery.getCustomer().getParcelCount();

            if(parcelCount >= parcelLimit) {
                delivery.setOverParcelFee(contract.getOverParcelFee());
                delivery.setOverParcel(true);
                delivery.getCustomer().setParcelCount(parcelCount + 1);
            } else {
                delivery.setOverParcelFee(0);
                delivery.setOverParcel(false);
                delivery.getCustomer().setParcelCount(parcelCount + 1);
            }

            int finalFee = contract.getBaseFee() + delivery.getOverWeightFee() + delivery.getOverParcelFee();
            delivery.setFinalFee(finalFee);

            List<DeliveryUpdateLog> logs = new ArrayList<>();

            if(prevOverWeightFee != delivery.getOverWeightFee()) {
                logs.add(DeliveryUpdateLog.builder()
                                .delivery(delivery)
                                .user(user)
                                .changedByUsername(username)
                                .type("over_weight_fee")
                                .prevData(String.valueOf(prevOverWeightFee))
                                .newData(String.valueOf(delivery.getOverWeightFee()))
                                .build());
            }

            if(prevIsOverWeight != delivery.isOverWeight()) {
                logs.add(DeliveryUpdateLog.builder()
                                .delivery(delivery)
                                .user(user)
                                .changedByUsername(username)
                                .type("is_over_weight")
                                .prevData(String.valueOf(prevIsOverWeight))
                                .newData(String.valueOf(delivery.isOverWeight()))
                        .build());
            }

            if(prevOverParcelFee != delivery.getOverParcelFee()) {
                logs.add(DeliveryUpdateLog.builder()
                                .delivery(delivery)
                                .user(user)
                                .changedByUsername(username)
                                .type("over_parcel_fee")
                                .prevData(String.valueOf(prevOverParcelFee))
                                .newData(String.valueOf(delivery.getOverParcelFee()))
                        .build());
            }

            if(prevIsOverParcel != delivery.isOverParcel()) {
                logs.add(DeliveryUpdateLog.builder()
                                .delivery(delivery)
                                .user(user)
                                .changedByUsername(username)
                                .type("is_over_parcel")
                                .prevData(String.valueOf(prevIsOverParcel))
                                .newData(String.valueOf(delivery.isOverParcel()))
                        .build());
            }

            if(prevFinalFee != delivery.getFinalFee()) {
                logs.add(DeliveryUpdateLog.builder()
                                .delivery(delivery)
                                .user(user)
                                .changedByUsername(username)
                                .type("final_fee")
                                .prevData(String.valueOf(prevFinalFee))
                                .newData(String.valueOf(delivery.getFinalFee()))
                        .build());
            }

            if(!logs.isEmpty()) {
                deliveryUpdateLogRepository.saveAll(logs);
            }
        }

        customerRepository.save(delivery.getCustomer());
        Delivery updatedDelivery = deliveryRepository.save(delivery);

        DeliveryStatusLog deliveryStatusLog = DeliveryStatusLog.builder()
                .delivery(delivery)
                .user(user)
                .changedByUsername(username)
                .changeReason(dto.getChangeReason())
                .prevStatus(prevStatus)
                .newStatus(updatedDelivery.getStatus())
                .build();

        deliveryStatusLogRepository.save(deliveryStatusLog);

        // 배송 상태 수정에 알림 넣음
        String alertMessage = "배송 # " + deliveryId + " 상태가 '" + deliveryStatusLog.getNewStatus() + "'로 변경되었습니다.";
        alertService.sendToUser(delivery.getCustomer().getUser().getId(), alertMessage);


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
                .createdAt(DateUtils.format(updatedDelivery.getCreatedAt()))
                .updatedAt(DateUtils.format(updatedDelivery.getUpdatedAt()))
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
    @Transactional
    public ResponseDto<Void> deleteDelivery(UserPrincipal userPrincipal, Long deliveryId) {
        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        if(!user.getRole().getName().equals(UserRole.ADMIN)) {
            return ResponseDto.fail("FORBIDDEN", ResponseMessage.NO_PERMISSION);
        }

        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        if(delivery.getStatus() == DeliveryStatus.DELETED) {
            return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        }

        delivery.setStatus(DeliveryStatus.DELETED);
        deliveryRepository.save(delivery);

        deleteLogService.createLog(TableRef.DELIVERY, deliveryId, user);

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
                .createdAt(DateUtils.format(delivery.getCreatedAt()))
                .updatedAt(DateUtils.format(delivery.getUpdatedAt()))
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
                .createdAt(DateUtils.format(delivery.getCreatedAt()))
                .updatedAt(DateUtils.format(delivery.getUpdatedAt()))
                .build();
    }
  
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

    @Override
    @Transactional
    public ResponseDto<List<CreateDeliveryResponseDto>> uploadDelivery(MultipartFile file, UserPrincipal userPrincipal) {
        List<Delivery> deliveries = parseAndSaveExcel(file, userPrincipal);

        List<CreateDeliveryResponseDto> responseDtos = null;

        responseDtos = deliveries.stream()
            .map(delivery -> CreateDeliveryResponseDto.builder()
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
                .createdAt(DateUtils.format(delivery.getCreatedAt()))
                .updatedAt(DateUtils.format(delivery.getUpdatedAt()))
                .build())
            .collect(Collectors.toList());

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDtos);
    }

    private List<Delivery> parseAndSaveExcel(MultipartFile file, UserPrincipal userPrincipal) {
        List<Delivery> savedDeliveries = new ArrayList<>();

        try (InputStream is = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String username = userPrincipal.getUsername();
                User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));
                Customer customer = customerRepository.findByUser(user).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));
                Contract contract = contractRepository.findByCustomerAndStatus(customer, ContractStatus.APPROVED).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

                Delivery delivery = Delivery.builder()
                    .contract(contract)
                    .customer(customer)
                    .requestDate(getDateTimeCellValue(row.getCell(0)))
                    .item(getStringCellValue(row.getCell(1)))
                    .weight(BigDecimal.valueOf(getNumericCellValue(row.getCell(2))))
                    .message(getStringCellValue(row.getCell(3)))
                    .isHidden(getBooleanCellValue(row, 4))
                    .status(DeliveryStatus.REQUESTED)
                    .pickupName(getStringCellValue(row.getCell(5)))
                    .pickupPhone(getStringCellValue(row.getCell(6)))
                    .pickupZipCode(getStringCellValue(row.getCell(7)))
                    .pickupAddress(getStringCellValue(row.getCell(8)))
                    .pickupAddressDetail(getStringCellValue(row.getCell(9)))
                    .recipientName(getStringCellValue(row.getCell(10)))
                    .recipientPhone(getStringCellValue(row.getCell(11)))
                    .recipientZipcode(getStringCellValue(row.getCell(12)))
                    .recipientAddress(getStringCellValue(row.getCell(13)))
                    .recipientAddressDetail(getStringCellValue(row.getCell(14)))
                    .finalFee(0)
                    .overWeightFee(0)
                    .overParcelFee(0)
                    .isOverWeight(false)
                    .isOverParcel(false)
                    .build();

                deliveryRepository.save(delivery);
                savedDeliveries.add(delivery);
            }
        } catch (Exception e) {
            throw new RuntimeException("엑셀 처리 실패: " + e.getMessage(), e);
        }

        return savedDeliveries;
    }

    private String getStringCellValue(Cell cell) {
        if (cell == null) return null;
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue();
            case NUMERIC: return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            default: return null;
        }
    }

    private double getNumericCellValue(Cell cell) {
        if (cell == null) return 0;
        switch (cell.getCellType()) {
            case NUMERIC: return cell.getNumericCellValue();
            case STRING:
                try { return Double.parseDouble(cell.getStringCellValue()); }
                catch (NumberFormatException e) { return 0; }
            default: return 0;
        }
    }

    private boolean getBooleanCellValue(Row row, int index) {
        Cell cell = row.getCell(index);
        if (cell == null) return false;
        return switch (cell.getCellType()) {
            case BOOLEAN -> cell.getBooleanCellValue();
            case STRING -> Boolean.parseBoolean(cell.getStringCellValue());
            case NUMERIC -> cell.getNumericCellValue() != 0;
            default -> false;
        };
    }

    public LocalDateTime getDateTimeCellValue(Cell cell) {
        if (cell == null) return null;

        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                return cell.getLocalDateTimeCellValue();
            } else if (cell.getCellType() == CellType.STRING) {
                return DateUtils.parse(cell.getStringCellValue());
            }
        } catch (Exception e) {
            System.err.println("Excel 날짜 변환 실패: " + e.getMessage());
        }
        return null;
    }
}

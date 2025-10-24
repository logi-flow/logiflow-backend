package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.constants.ResponseCode;
import com.logi_flow.backend.common.constants.ResponseMessage;
import com.logi_flow.backend.common.enums.AllocationStatus;
import com.logi_flow.backend.common.enums.user.UserRole;
import com.logi_flow.backend.common.util.DateUtils;
import com.logi_flow.backend.common.util.SortUtils;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.schedule.request.UpdateScheduleRequestDto;
import com.logi_flow.backend.dto.schedule.response.GetAllScheduleResponseDto;
import com.logi_flow.backend.dto.schedule.response.GetScheduleDetailResponseDto;
import com.logi_flow.backend.dto.schedule.response.UpdateScheduleResponseDto;
import com.logi_flow.backend.entity.*;
import com.logi_flow.backend.repository.*;
import com.logi_flow.backend.service.AlertService;
import com.logi_flow.backend.service.ScheduleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;
    private final AllocationRepository allocationRepository;
    private final AllocationStatusLogRepository allocationStatusLogRepository;
    private final DriverRepository driverRepository;

    private final AlertService alertService;

    @Override
    @Transactional
    public ResponseDto<UpdateScheduleResponseDto> updateSchedule(UserPrincipal userPrincipal, Long scheduleId, UpdateScheduleRequestDto dto) {
        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));
        Allocation allocation = schedule.getAllocation();
        AllocationStatus prevStatus = allocation.getStatus();

        if(dto.getStatus() != allocation.getStatus()) {
            allocation.setStatus(dto.getStatus());
        }

        Allocation updatedAllocation = allocationRepository.save(allocation);

        AllocationStatusLog allocationStatusLog = AllocationStatusLog.builder()
                .allocation(allocation)
                .user(user)
                .changedByUsername(username)
                .changeReason(dto.getChangeReason())
                .prevStatus(prevStatus)
                .newStatus(updatedAllocation.getStatus())
                .build();

        allocationStatusLogRepository.save(allocationStatusLog);


        if(updatedAllocation.getStatus().equals(AllocationStatus.IN_PROGRESS)) {

            if(schedule.getDepartureTime() == null) {
                schedule.setDepartureTime(dto.getDepartureTime());
            }

            if(schedule.getArrivalTime() == null) {
                schedule.setArrivalTime(dto.getArrivalTime());
            }

            scheduleRepository.save(schedule);
        }

        UpdateScheduleResponseDto data = UpdateScheduleResponseDto.builder()
                .id(schedule.getId())
                .allocationId(schedule.getAllocation().getId())
                .allocationDate(schedule.getAllocationDate())
                .departureTime(DateUtils.format(schedule.getDepartureTime()))
                .arrivalTime(DateUtils.format(schedule.getArrivalTime()))
                .createdAt(DateUtils.format(schedule.getCreatedAt()))
                .updatedAt(DateUtils.format(schedule.getUpdatedAt()))
                .build();

        String alertMessage = "배송 #" + allocation.getId() + " 상태가 " + updatedAllocation.getStatus() + "로 변경되었습니다.";
        List<User> contractManagerList = userRepository.findByRoleName(UserRole.ALLOCATIONS_MANAGER);

        if(contractManagerList != null && !contractManagerList.isEmpty()) {
            for(User manager : contractManagerList) {
                alertService.sendToUser(manager.getId(), alertMessage);
            }
        } else {
            System.out.println("알림을 보낼 계약 관리자가 없음.");
        }

        // 여기를 수정합니다.
        User customerUser = null;
        if (allocation.getDelivery() != null) {
            customerUser = allocation.getDelivery().getCustomer().getUser();
        } else if (allocation.getReturnDelivery() != null) {
            customerUser = allocation.getReturnDelivery().getDelivery().getCustomer().getUser();
        }

        if (customerUser != null) {
            alertService.sendToUser(customerUser.getId(), alertMessage);
        }

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public Page<GetAllScheduleResponseDto> getAllSchedule(int page, int size, String sort) {
        Page<GetAllScheduleResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<Schedule> schedules = scheduleRepository.findAll(pageable);

        data = schedules.map(this::toGetAllScheduleResponseDto);

        return data;
    }

    @Override
    public ResponseDto<GetScheduleDetailResponseDto> getSchedule(Long scheduleId) {

        Schedule schedule = scheduleRepository.findByIdWithDetails(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        Allocation allocation = schedule.getAllocation();

        Long deliveryId;
        CollectionSite collectionSite;
        String recipientName, recipientPhone, recipientAddress, recipientAddressDetail, recipientZipcode;
        String deliveryType;

        if (allocation.getDelivery() != null) {
            deliveryType = "delivery";
            Delivery delivery = allocation.getDelivery();
            deliveryId = delivery.getId();
            collectionSite = delivery.getCollectionSite();
            recipientName = delivery.getRecipientName();
            recipientPhone = delivery.getRecipientPhone();
            recipientAddress = delivery.getRecipientAddress();
            recipientAddressDetail = delivery.getRecipientAddressDetail();
            recipientZipcode = delivery.getRecipientZipcode();
        } else if (allocation.getReturnDelivery() != null) {
            deliveryType = "returnDelivery";
            ReturnDelivery returnDelivery = allocation.getReturnDelivery();
            deliveryId = returnDelivery.getId();
            collectionSite = returnDelivery.getDelivery().getCollectionSite();
            recipientName = returnDelivery.getDelivery().getRecipientName();
            recipientPhone = returnDelivery.getDelivery().getRecipientPhone();
            recipientAddress = returnDelivery.getDelivery().getRecipientAddress();
            recipientAddressDetail = returnDelivery.getDelivery().getRecipientAddressDetail();
            recipientZipcode = returnDelivery.getDelivery().getRecipientZipcode();
        } else {
            throw new EntityNotFoundException("해당 배차에 연결된 배송 또는 반품 정보가 없음");
        }

        GetScheduleDetailResponseDto data = GetScheduleDetailResponseDto.builder()
                .id(schedule.getId())
                .allocationId(allocation.getId())
                .status(String.valueOf(allocation.getStatus()))
                .deliveryType(deliveryType)
                .deliveryId(deliveryId)
                .collectionSitePhoneNumber(collectionSite.getPhoneNumber())
                .collectionSiteAddress(collectionSite.getAddress())
                .collectionSiteAddressDetail(collectionSite.getAddressDetail())
                .collectionSiteZipcode(collectionSite.getZipCode())
                .recipientName(recipientName)
                .recipientPhoneNumber(recipientPhone)
                .recipientAddress(recipientAddress)
                .recipientAddressDetail(recipientAddressDetail)
                .recipientZipcode(recipientZipcode)
                .allocationDate(schedule.getAllocationDate())
                .departureTime(DateUtils.format(schedule.getDepartureTime()))
                .arrivalTime(DateUtils.format(schedule.getArrivalTime()))
                .driverId(allocation.getAssignment().getDriver().getId())
                .driverName(allocation.getAssignment().getDriver().getName())
                .driverPhone(allocation.getAssignment().getDriver().getPhoneNumber())
                .createdAt(DateUtils.format(schedule.getCreatedAt()))
                .updatedAt(DateUtils.format(schedule.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public Page<GetAllScheduleResponseDto> getScheduleByDriverId(Long driverId, int page, int size, String sort) {
        Page<GetAllScheduleResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<Schedule> schedules = scheduleRepository.findByDriverId(driverId, pageable);

        data = schedules.map(this::toGetAllScheduleResponseDto);
        return data;
    }

    @Override
    public Page<GetAllScheduleResponseDto> getMySchedules(UserPrincipal userPrincipal, int page, int size, String sort) {
        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));
        Driver driver = driverRepository.findByUser(user).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Page<GetAllScheduleResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<Schedule> schedules = scheduleRepository.findByDriverId(driver.getId(), pageable);

        data = schedules.map(this::toGetAllScheduleResponseDto);
        return data;
    }

    private GetAllScheduleResponseDto toGetAllScheduleResponseDto(Schedule schedule) {
        return GetAllScheduleResponseDto.builder()
                .id(schedule.getId())
                .allocationId(schedule.getAllocation().getId())
                .allocationDate(schedule.getAllocationDate())
                .departureTime(DateUtils.format(schedule.getDepartureTime()))
                .arrivalTime(DateUtils.format(schedule.getArrivalTime()))
                .createdAt(DateUtils.format(schedule.getCreatedAt()))
                .updatedAt(DateUtils.format(schedule.getUpdatedAt()))
                .build();
    }
}

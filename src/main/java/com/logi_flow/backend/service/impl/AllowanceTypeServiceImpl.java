package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.constants.ResponseCode;
import com.logi_flow.backend.common.constants.ResponseMessage;
import com.logi_flow.backend.common.enums.AllowanceTypeStatus;
import com.logi_flow.backend.common.enums.DriverPayrollStatus;
import com.logi_flow.backend.common.enums.TableRef;
import com.logi_flow.backend.common.util.DateUtils;
import com.logi_flow.backend.common.util.SortUtils;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.allowanceType.request.CreateAllowanceTypeRequestDto;
import com.logi_flow.backend.dto.allowanceType.request.UpdateAllowanceTypeRequestDto;
import com.logi_flow.backend.dto.allowanceType.response.CreateAllowanceTypeResponseDto;
import com.logi_flow.backend.dto.allowanceType.response.GetAllAllowanceTypeResponseDto;
import com.logi_flow.backend.dto.allowanceType.response.GetAllowanceTypeDetailResponseDto;
import com.logi_flow.backend.dto.allowanceType.response.UpdateAllowanceTypeResponseDto;
import com.logi_flow.backend.entity.AllowanceType;
import com.logi_flow.backend.entity.AllowanceTypeUpdateLog;
import com.logi_flow.backend.entity.User;
import com.logi_flow.backend.repository.AllowanceTypeRepository;
import com.logi_flow.backend.repository.AllowanceTypeUpdateLogRepository;
import com.logi_flow.backend.repository.DriverAllowanceRepository;
import com.logi_flow.backend.repository.UserRepository;
import com.logi_flow.backend.service.AllowanceTypeService;
import com.logi_flow.backend.service.DeleteLogService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AllowanceTypeServiceImpl implements AllowanceTypeService {

    private final AllowanceTypeRepository allowanceTypeRepository;
    private final UserRepository userRepository;
    private final DriverAllowanceRepository driverAllowanceRepository;
    private final AllowanceTypeUpdateLogRepository allowanceTypeUpdateLogRepository;
    private final DeleteLogService deleteLogService;

    @Override
    @Transactional
    public ResponseDto<CreateAllowanceTypeResponseDto> createAllowanceType(CreateAllowanceTypeRequestDto dto) {
        CreateAllowanceTypeResponseDto data = null;

        String code = dto.getCode().trim().toUpperCase();

        if (allowanceTypeRepository.findByCodeAndStatus(code, AllowanceTypeStatus.ACTIVE).isPresent()) {
            return ResponseDto.fail(ResponseCode.EXISTS_TYPE_CODE, ResponseMessage.EXISTS_TYPE_CODE);
        }

        Optional<AllowanceType> deletedAllowanceType = allowanceTypeRepository.findByCodeAndStatus(code, AllowanceTypeStatus.DELETED);

        if (deletedAllowanceType.isPresent()) {
            AllowanceType restoreAllowanceType = deletedAllowanceType.get();
            restoreAllowanceType.setName(dto.getName());
            restoreAllowanceType.setDescription(dto.getDescription());
            restoreAllowanceType.setActive(true);
            restoreAllowanceType.setStatus(AllowanceTypeStatus.ACTIVE);
            AllowanceType savedAllowanceType = allowanceTypeRepository.save(restoreAllowanceType);

            deleteLogService.removeIfExists(TableRef.ALLOWANCE_TYPE, savedAllowanceType.getId());

            data = toCreateAllowanceTypeResponseDto(savedAllowanceType);

            return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
        }

        AllowanceType newAllowanceType = AllowanceType.builder()
                .code(code)
                .name(dto.getName())
                .description(dto.getDescription())
                .isActive(true)
                .status(AllowanceTypeStatus.ACTIVE)
                .build();

        AllowanceType savedAllowanceType = allowanceTypeRepository.save(newAllowanceType);

        data = toCreateAllowanceTypeResponseDto(savedAllowanceType);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public Page<GetAllAllowanceTypeResponseDto> getAllAllowanceType(int page, int size, String sort) {
        Page<GetAllAllowanceTypeResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<AllowanceType> allowanceTypes = allowanceTypeRepository.findByStatus(AllowanceTypeStatus.ACTIVE, pageable);

        data = allowanceTypes.map(this::toGetAllAllowanceTypeResponseDto);

        return data;
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseDto<GetAllowanceTypeDetailResponseDto> getAllowanceTypeDetail(Long allowanceTypeId) {
        GetAllowanceTypeDetailResponseDto data = null;
        AllowanceType allowanceType = getAllowanceType(allowanceTypeId);

        if (allowanceType.getStatus() == AllowanceTypeStatus.DELETED) {
            return ResponseDto.fail(ResponseCode.ALREADY_DELETED, ResponseMessage.ALREADY_DELETED);
        }

        data = GetAllowanceTypeDetailResponseDto.builder()
                .id(allowanceType.getId())
                .code(allowanceType.getCode())
                .name(allowanceType.getName())
                .description(allowanceType.getDescription())
                .isActive(allowanceType.isActive())
                .createdAt(DateUtils.format(allowanceType.getCreatedAt()))
                .updatedAt(DateUtils.format(allowanceType.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    @Transactional
    public ResponseDto<UpdateAllowanceTypeResponseDto> updateAllowanceType(UserPrincipal userPrincipal, Long allowanceTypeId, UpdateAllowanceTypeRequestDto dto) {
        UpdateAllowanceTypeResponseDto data = null;

        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        AllowanceType savedAllowanceType = getAllowanceType(allowanceTypeId);
        List<AllowanceTypeUpdateLog> logs = new ArrayList<>();

        if (savedAllowanceType.getCode().equals("BASE")) {
            return ResponseDto.fail(ResponseCode.SYSTEM_ITEM_IMMUTABLE, ResponseMessage.SYSTEM_ITEM_IMMUTABLE);
        }

        if (savedAllowanceType.getStatus() == AllowanceTypeStatus.DELETED) {
            return ResponseDto.fail(ResponseCode.ALREADY_DELETED, ResponseMessage.ALREADY_DELETED);
        }

        if (!dto.getName().equals(savedAllowanceType.getName())) {
            String prevData = savedAllowanceType.getName();
            savedAllowanceType.setName(dto.getName());
            logs.add(createUpdateLog(user, savedAllowanceType, "name", prevData, savedAllowanceType.getName()));
        }

        if (!Objects.equals(dto.getDescription(), savedAllowanceType.getDescription())) {
            String prevData = savedAllowanceType.getDescription();
            savedAllowanceType.setDescription(dto.getDescription());
            logs.add(createUpdateLog(user, savedAllowanceType, "description", prevData, savedAllowanceType.getDescription()));
        }

        if (dto.isActive() != savedAllowanceType.isActive()) {
            String prevData = String.valueOf(savedAllowanceType.isActive());
            savedAllowanceType.setActive(dto.isActive());
            logs.add(createUpdateLog(user, savedAllowanceType, "is_active", prevData, String.valueOf(savedAllowanceType.isActive())));
        }

        AllowanceType updatedAllowanceType = allowanceTypeRepository.save(savedAllowanceType);

        if (!logs.isEmpty()) {
            allowanceTypeUpdateLogRepository.saveAll(logs);
        }

        data = UpdateAllowanceTypeResponseDto.builder()
                .id(updatedAllowanceType.getId())
                .code(updatedAllowanceType.getCode())
                .name(updatedAllowanceType.getName())
                .description(updatedAllowanceType.getDescription())
                .isActive(updatedAllowanceType.isActive())
                .status(updatedAllowanceType.getStatus())
                .createdAt(DateUtils.format(updatedAllowanceType.getCreatedAt()))
                .updatedAt(DateUtils.format(updatedAllowanceType.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    @Transactional
    public ResponseDto<Void> deleteAllowanceType(UserPrincipal userPrincipal, Long allowanceTypeId) {
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        AllowanceType allowanceType = getAllowanceType(allowanceTypeId);
        boolean usedInConfirmedPayroll = driverAllowanceRepository.existsByAllowanceTypeIdAndDriverPayroll_Status(allowanceTypeId, DriverPayrollStatus.CONFIRMED);

        if (usedInConfirmedPayroll) {
            return ResponseDto.fail(ResponseCode.NOT_DELETE_USED_TYPE, ResponseMessage.NOT_DELETE_USED_TYPE);
        }

        if ("BASE".equals(allowanceType.getCode())) {
            return ResponseDto.fail(ResponseCode.SYSTEM_ITEM_IMMUTABLE, ResponseMessage.SYSTEM_ITEM_IMMUTABLE);
        }

        if (allowanceType.getStatus() == AllowanceTypeStatus.DELETED) {
            return ResponseDto.fail(ResponseCode.ALREADY_DELETED, ResponseMessage.ALREADY_DELETED);
        }

        allowanceType.setActive(false);
        allowanceType.setStatus(AllowanceTypeStatus.DELETED);
        allowanceTypeRepository.save(allowanceType);

        deleteLogService.createLog(TableRef.ALLOWANCE_TYPE, allowanceTypeId, user);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    @Override
    @Transactional(readOnly = true)
    public AllowanceType getAllowanceType(Long allowanceTypeId) {
        return allowanceTypeRepository.findById(allowanceTypeId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));
    }

    private CreateAllowanceTypeResponseDto toCreateAllowanceTypeResponseDto(AllowanceType allowanceType) {
        return CreateAllowanceTypeResponseDto.builder()
                .id(allowanceType.getId())
                .code(allowanceType.getCode())
                .name(allowanceType.getName())
                .description(allowanceType.getDescription())
                .isActive(allowanceType.isActive())
                .status(allowanceType.getStatus())
                .createdAt(DateUtils.format(allowanceType.getCreatedAt()))
                .updatedAt(DateUtils.format(allowanceType.getUpdatedAt()))
                .build();
    }

    private GetAllAllowanceTypeResponseDto toGetAllAllowanceTypeResponseDto(AllowanceType allowanceType) {
        return GetAllAllowanceTypeResponseDto.builder()
                .id(allowanceType.getId())
                .code(allowanceType.getCode())
                .name(allowanceType.getName())
                .isActive(allowanceType.isActive())
                .createdAt(DateUtils.format(allowanceType.getCreatedAt()))
                .updatedAt(DateUtils.format(allowanceType.getUpdatedAt()))
                .build();
    }

    private AllowanceTypeUpdateLog createUpdateLog(User user, AllowanceType savedAllowanceType, String type, String prevData, String newData) {
        return AllowanceTypeUpdateLog.builder()
                .allowanceType(savedAllowanceType)
                .user(user)
                .changedByUsername(user.getUsername())
                .type(type)
                .prevData(prevData)
                .newData(newData)
                .build();
    }
}

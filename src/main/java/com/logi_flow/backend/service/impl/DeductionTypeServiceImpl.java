package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.constants.ResponseCode;
import com.logi_flow.backend.common.constants.ResponseMessage;
import com.logi_flow.backend.common.enums.DeductionTypeStatus;
import com.logi_flow.backend.common.enums.DriverPayrollStatus;
import com.logi_flow.backend.common.enums.TableRef;
import com.logi_flow.backend.common.util.DateUtils;
import com.logi_flow.backend.common.util.SortUtils;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.deductionType.request.CreateDeductionTypeRequestDto;
import com.logi_flow.backend.dto.deductionType.request.UpdateDeductionTypeRequestDto;
import com.logi_flow.backend.dto.deductionType.response.CreateDeductionTypeResponseDto;
import com.logi_flow.backend.dto.deductionType.response.GetAllDeductionTypeResponseDto;
import com.logi_flow.backend.dto.deductionType.response.GetDeductionTypeDetailResponseDto;
import com.logi_flow.backend.dto.deductionType.response.UpdateDeductionTypeResponseDto;
import com.logi_flow.backend.entity.DeductionType;
import com.logi_flow.backend.entity.DeductionTypeUpdateLog;
import com.logi_flow.backend.entity.User;
import com.logi_flow.backend.repository.DeductionTypeRepository;
import com.logi_flow.backend.repository.DeductionTypeUpdateLogRepository;
import com.logi_flow.backend.repository.DriverDeductionRepository;
import com.logi_flow.backend.repository.UserRepository;
import com.logi_flow.backend.service.DeductionTypeService;
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
public class DeductionTypeServiceImpl implements DeductionTypeService {

    private final DeductionTypeRepository deductionTypeRepository;
    private final UserRepository userRepository;
    private final DriverDeductionRepository driverDeductionRepository;
    private final DeductionTypeUpdateLogRepository deductionTypeUpdateLogRepository;
    private final DeleteLogService deleteLogService;

    @Override
    @Transactional
    public ResponseDto<CreateDeductionTypeResponseDto> createDeductionType(CreateDeductionTypeRequestDto dto) {
        CreateDeductionTypeResponseDto data = null;

        String code = dto.getCode().trim().toUpperCase();

        if (deductionTypeRepository.findByCodeAndStatus(code, DeductionTypeStatus.ACTIVE).isPresent()) {
            return ResponseDto.fail(ResponseCode.EXISTS_TYPE_CODE, ResponseMessage.EXISTS_TYPE_CODE);
        }

        Optional<DeductionType> deletedDeductionType = deductionTypeRepository.findByCodeAndStatus(code, DeductionTypeStatus.DELETED);

        if (deletedDeductionType.isPresent()) {
            DeductionType restoreDeductionType = deletedDeductionType.get();
            restoreDeductionType.setName(dto.getName());
            restoreDeductionType.setDescription(dto.getDescription());
            restoreDeductionType.setActive(true);
            restoreDeductionType.setStatus(DeductionTypeStatus.ACTIVE);
            DeductionType savedDeductionType = deductionTypeRepository.save(restoreDeductionType);

            deleteLogService.removeIfExists(TableRef.DEDUCTION_TYPE, restoreDeductionType.getId());

            data = toCreateDeductionTypeResponseDto(savedDeductionType);

            return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
        }

        DeductionType newDeductionType = DeductionType.builder()
                .code(code)
                .name(dto.getName())
                .description(dto.getDescription())
                .isActive(true)
                .status(DeductionTypeStatus.ACTIVE)
                .build();

        DeductionType savedDeductionType = deductionTypeRepository.save(newDeductionType);

        data = toCreateDeductionTypeResponseDto(savedDeductionType);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public Page<GetAllDeductionTypeResponseDto> getAllDeductionType(int page, int size, String sort) {
        Page<GetAllDeductionTypeResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<DeductionType> deductionTypes = deductionTypeRepository.findByStatus(DeductionTypeStatus.ACTIVE, pageable);

        data = deductionTypes.map(this::toGetAllDeductionTypeResponseDto);

        return data;
    }

    @Override
    public ResponseDto<GetDeductionTypeDetailResponseDto> getDeductionTypeDetail(Long deductionTypeId) {
        GetDeductionTypeDetailResponseDto data = null;
        DeductionType deductionType = getDeductionType(deductionTypeId);

        if (deductionType.getStatus() == DeductionTypeStatus.DELETED) {
            return ResponseDto.fail(ResponseCode.ALREADY_DELETED, ResponseMessage.ALREADY_DELETED);
        }

        data = GetDeductionTypeDetailResponseDto.builder()
                .id(deductionType.getId())
                .code(deductionType.getCode())
                .name(deductionType.getName())
                .description(deductionType.getDescription())
                .isActive(deductionType.isActive())
                .createdAt(DateUtils.format(deductionType.getCreatedAt()))
                .updatedAt(DateUtils.format(deductionType.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    @Transactional
    public ResponseDto<UpdateDeductionTypeResponseDto> updateDeductionType(UserPrincipal userPrincipal, Long deductionTypeId, UpdateDeductionTypeRequestDto dto) {
        UpdateDeductionTypeResponseDto data = null;

        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        DeductionType savedDeductionType = getDeductionType(deductionTypeId);
        List<DeductionTypeUpdateLog> logs = new ArrayList<>();

        if (!dto.getName().equals(savedDeductionType.getName())) {
            String prevData = savedDeductionType.getName();
            savedDeductionType.setName(dto.getName());
            logs.add(createUpdateLog(user, savedDeductionType, "name", prevData, savedDeductionType.getName()));
        }

        if (!Objects.equals(dto.getDescription(), savedDeductionType.getDescription())) {
            String prevData = savedDeductionType.getDescription();
            savedDeductionType.setDescription(dto.getDescription());
            logs.add(createUpdateLog(user, savedDeductionType, "description", prevData, savedDeductionType.getDescription()));
        }

        if (dto.isActive() != savedDeductionType.isActive()) {
            String prevData = String.valueOf(savedDeductionType.isActive());
            savedDeductionType.setActive(dto.isActive());
            logs.add(createUpdateLog(user, savedDeductionType, "is_active", prevData, String.valueOf(savedDeductionType.isActive())));
        }

        DeductionType updatedDeductionType = deductionTypeRepository.save(savedDeductionType);

        if (!logs.isEmpty()) {
            deductionTypeUpdateLogRepository.saveAll(logs);
        }

        data = UpdateDeductionTypeResponseDto.builder()
                .id(updatedDeductionType.getId())
                .code(updatedDeductionType.getCode())
                .name(updatedDeductionType.getName())
                .description(updatedDeductionType.getDescription())
                .isActive(updatedDeductionType.isActive())
                .status(updatedDeductionType.getStatus())
                .createdAt(DateUtils.format(updatedDeductionType.getCreatedAt()))
                .updatedAt(DateUtils.format(updatedDeductionType.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    @Transactional
    public ResponseDto<Void> deleteDeductionType(UserPrincipal userPrincipal, Long deductionTypeId) {
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        DeductionType deductionType = getDeductionType(deductionTypeId);
        boolean usedInConfirmedPayroll = driverDeductionRepository.existsByDeductionTypeIdAndDriverPayroll_Status(deductionTypeId, DriverPayrollStatus.CONFIRMED);

        if (usedInConfirmedPayroll) {
            return ResponseDto.fail(ResponseCode.NOT_DELETE_USED_TYPE, ResponseMessage.NOT_DELETE_USED_TYPE);
        }

        if (deductionType.getStatus() == DeductionTypeStatus.DELETED) {
            return ResponseDto.fail(ResponseCode.ALREADY_DELETED, ResponseMessage.ALREADY_DELETED);
        }

        deductionType.setActive(false);
        deductionType.setStatus(DeductionTypeStatus.DELETED);
        deductionTypeRepository.save(deductionType);

        deleteLogService.createLog(TableRef.DEDUCTION_TYPE, deductionTypeId, user);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    @Override
    public DeductionType getDeductionType(Long deductionTypeId) {
        return deductionTypeRepository.findById(deductionTypeId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));
    }

    private CreateDeductionTypeResponseDto toCreateDeductionTypeResponseDto(DeductionType deductionType) {
        return CreateDeductionTypeResponseDto.builder()
                .id(deductionType.getId())
                .code(deductionType.getCode())
                .name(deductionType.getName())
                .description(deductionType.getDescription())
                .isActive(deductionType.isActive())
                .status(deductionType.getStatus())
                .createdAt(DateUtils.format(deductionType.getCreatedAt()))
                .updatedAt(DateUtils.format(deductionType.getUpdatedAt()))
                .build();
    }

    private GetAllDeductionTypeResponseDto toGetAllDeductionTypeResponseDto(DeductionType deductionType) {
        return GetAllDeductionTypeResponseDto.builder()
                .id(deductionType.getId())
                .code(deductionType.getCode())
                .name(deductionType.getName())
                .isActive(deductionType.isActive())
                .createdAt(DateUtils.format(deductionType.getCreatedAt()))
                .updatedAt(DateUtils.format(deductionType.getUpdatedAt()))
                .build();
    }

    private DeductionTypeUpdateLog createUpdateLog(User user, DeductionType savedDeductionType, String type, String prevData, String newData) {
        return DeductionTypeUpdateLog.builder()
                .deductionType(savedDeductionType)
                .user(user)
                .changedByUsername(user.getUsername())
                .type(type)
                .prevData(prevData)
                .newData(newData)
                .build();
    }
}

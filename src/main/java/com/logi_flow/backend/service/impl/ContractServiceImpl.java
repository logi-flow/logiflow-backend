package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.constants.ResponseCode;
import com.logi_flow.backend.common.constants.ResponseMessage;
import com.logi_flow.backend.common.enums.ContractStatus;
import com.logi_flow.backend.common.enums.TableRef;
import com.logi_flow.backend.common.enums.user.UserRole;
import com.logi_flow.backend.common.util.DateUtils;
import com.logi_flow.backend.common.util.SortUtils;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.contract.request.CreateContractRequestDto;
import com.logi_flow.backend.dto.contract.request.UpdateContractRequestDto;
import com.logi_flow.backend.dto.contract.request.UpdateContractStatusRequestDto;
import com.logi_flow.backend.dto.contract.response.*;
import com.logi_flow.backend.entity.*;
import com.logi_flow.backend.repository.*;
import com.logi_flow.backend.service.AlertService;
import com.logi_flow.backend.service.ContractService;
import com.logi_flow.backend.service.DeleteLogService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ContractServiceImpl implements ContractService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final ContractRepository contractRepository;
    private final ContractUpdateLogRepository contractUpdateLogRepository;
    private final ContractStatusLogRepository contractStatusLogRepository;

    private final DeleteLogService  deleteLogService;
    private final AlertService alertService;

    @Override
    @Transactional
    public ResponseDto<CreateContractResponseDto> createContract(UserPrincipal userPrincipal, Long customerId, CreateContractRequestDto dto) {
        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        Contract newContract = Contract.builder()
                .customer(customer)
                .status(ContractStatus.PENDING)
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .baseFee(dto.getBaseFee())
                .weightLimitKg(dto.getWeightLimitKg())
                .parcelLimit(dto.getParcelLimit())
                .overWeightFeePerKg(dto.getOverWeightFeePerKg())
                .overParcelFee(dto.getOverParcelFee())
                .specialTerms(dto.getSpecialTerms())
                .build();

        contractRepository.save(newContract);

        String alertMessage = "새로운 계약이 생성되었습니다.";
        alertService.sendToUser(customer.getUser().getId(), alertMessage);

        CreateContractResponseDto data = CreateContractResponseDto.builder()
                .id(newContract.getId())
                .customerId(newContract.getCustomer().getId())
                .status(newContract.getStatus())
                .startDate(newContract.getStartDate())
                .endDate(newContract.getEndDate())
                .baseFee(newContract.getBaseFee())
                .weightLimitKg(newContract.getWeightLimitKg())
                .parcelLimit(newContract.getParcelLimit())
                .overWeightFeePerKg(newContract.getOverWeightFeePerKg())
                .overParcelFee(newContract.getOverParcelFee())
                .specialTerms(newContract.getSpecialTerms())
                .createdAt(DateUtils.format(newContract.getCreatedAt()))
                .updatedAt(DateUtils.format(newContract.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    @Transactional
    public ResponseDto<UpdateContractResponseDto> updateContract(UserPrincipal userPrincipal, Long contractId, UpdateContractRequestDto dto) {
        Contract contract = contractRepository.findById(contractId).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        List<ContractUpdateLog> logs = new ArrayList<>();

        if(!dto.getStartDate().equals(contract.getStartDate())) {
            logs.add(buildLog(contract, user, username, "start_date", contract.getStartDate().toString(), dto.getStartDate().toString()));
            contract.setStartDate(dto.getStartDate());
        }

        if(!dto.getEndDate().equals(contract.getEndDate())) {
            logs.add(buildLog(contract, user, username, "end_date", contract.getEndDate().toString(), dto.getEndDate().toString()));
            contract.setEndDate(dto.getEndDate());
        }

        if(dto.getBaseFee() != contract.getBaseFee()) {
            logs.add(buildLog(contract, user, username, "base_fee", String.valueOf(contract.getBaseFee()), String.valueOf(dto.getBaseFee())));
            contract.setBaseFee(dto.getBaseFee());
        }

        if(dto.getWeightLimitKg() != contract.getWeightLimitKg()) {
            logs.add(buildLog(contract, user, username, "weight_limit_kg", String.valueOf(contract.getWeightLimitKg()), String.valueOf(dto.getWeightLimitKg())));
            contract.setWeightLimitKg(dto.getWeightLimitKg());
        }

        if(dto.getParcelLimit() != contract.getParcelLimit()) {
            logs.add(buildLog(contract, user, username, "parcel_limit", String.valueOf(contract.getParcelLimit()), String.valueOf(dto.getParcelLimit())));
            contract.setParcelLimit(dto.getParcelLimit());
        }

        if(dto.getOverWeightFeePerKg() != contract.getOverWeightFeePerKg()) {
            logs.add(buildLog(contract, user, username, "over_weight_fee_per_kg", String.valueOf(contract.getOverWeightFeePerKg()), String.valueOf(dto.getOverWeightFeePerKg())));
            contract.setOverWeightFeePerKg(dto.getOverWeightFeePerKg());
        }

        if(dto.getOverParcelFee() != contract.getOverParcelFee()) {
            logs.add(buildLog(contract, user, username, "over_parcel_fee", String.valueOf(contract.getOverParcelFee()), String.valueOf(dto.getOverParcelFee())));
            contract.setOverParcelFee(dto.getOverParcelFee());
        }

        if(dto.getSpecialTerms() != null && !dto.getSpecialTerms().equals(contract.getSpecialTerms())) {
            logs.add(buildLog(contract, user, username, "special_terms", contract.getSpecialTerms() != null ? contract.getSpecialTerms() : null, dto.getSpecialTerms()));
            contract.setSpecialTerms(dto.getSpecialTerms());
        }

        Contract updatedContract = contractRepository.save(contract);
        contractUpdateLogRepository.saveAll(logs);

        String alertMessage = "계약 #" + contractId + "의 정보가 수정되었습니다.";
        alertService.sendToUser(contract.getCustomer().getUser().getId(), alertMessage);

        UpdateContractResponseDto data = UpdateContractResponseDto.builder()
                .id(updatedContract.getId())
                .customerId(updatedContract.getCustomer().getId())
                .status(updatedContract.getStatus())
                .startDate(updatedContract.getStartDate())
                .endDate(updatedContract.getEndDate())
                .baseFee(updatedContract.getBaseFee())
                .weightLimitKg(updatedContract.getWeightLimitKg())
                .parcelLimit(updatedContract.getParcelLimit())
                .overWeightFeePerKg(updatedContract.getOverWeightFeePerKg())
                .overParcelFee(updatedContract.getOverParcelFee())
                .specialTerms(updatedContract.getSpecialTerms())
                .createdAt(DateUtils.format(updatedContract.getCreatedAt()))
                .updatedAt(DateUtils.format(updatedContract.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    @Transactional
    public ResponseDto<UpdateContractStatusResponseDto> updateContractStatus(UserPrincipal userPrincipal, Long contractId, UpdateContractStatusRequestDto dto) {
        Contract contract = contractRepository.findById(contractId).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));
        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Customer customer = customerRepository.findByUser(user).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        if(!contract.getCustomer().getId().equals(customer.getId())) {
            return ResponseDto.fail("FORBIDDEN", ResponseMessage.NO_PERMISSION);
        }

        ContractStatus prevStatus = contract.getStatus();

        if(dto.getStatus() != prevStatus) {
            contract.setStatus(dto.getStatus());
        }

        Contract updatedContract = contractRepository.save(contract);

        ContractStatusLog contractStatusLog = ContractStatusLog.builder()
                .contract(contract)
                .changedBy(user)
                .changedByUsername(username)
                .changeReason(dto.getChangedReason())
                .prevStatus(prevStatus)
                .newStatus(updatedContract.getStatus())
                .build();

        contractStatusLogRepository.save(contractStatusLog);

        UpdateContractStatusResponseDto data = UpdateContractStatusResponseDto.builder()
                .id(updatedContract.getId())
                .customerId(updatedContract.getCustomer().getId())
                .status(updatedContract.getStatus())
                .changedBy(user.getId())
                .changedByUsername(username)
                .changedReason(contractStatusLog.getChangeReason())
                .prevStatus(prevStatus)
                .newStatus(updatedContract.getStatus())
                .createdAt(DateUtils.format(updatedContract.getCreatedAt()))
                .updatedAt(DateUtils.format(updatedContract.getUpdatedAt()))
                .build();

        String alertMessage = "계약 #" + contractId + " 의 상태가 " + updatedContract.getStatus() + "로 수정되었습니다.";

        List<User> contractManagerList = userRepository.findByRoleName(UserRole.CONTRACTS_MANAGER);

        if(contractManagerList != null && !contractManagerList.isEmpty()) {
            for(User manager : contractManagerList) {
                alertService.sendToUser(manager.getId(), alertMessage);
            }
        } else {
            throw new IllegalArgumentException(ResponseMessage.USER_NOT_FOUND);
        }

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public Page<GetAllContractResponseDto> getAllContract(UserPrincipal userPrincipal, int page, int size, String sort) {
        Page<GetAllContractResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<Contract> contracts = contractRepository.findAll(pageable);

        data = contracts.map(this::toGetAllContractResponseDto);

        return data;
    }

    @Override
    public ResponseDto<GetContractDetailResponseDto> getContractDetail(UserPrincipal userPrincipal, Long contractId) {
        Contract contract = contractRepository.findById(contractId).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        GetContractDetailResponseDto data = GetContractDetailResponseDto.builder()
                .id(contract.getId())
                .customerId(contract.getCustomer().getId())
                .customerName(contract.getCustomer().getName())
                .status(contract.getStatus())
                .startDate(contract.getStartDate())
                .endDate(contract.getEndDate())
                .baseFee(contract.getBaseFee())
                .weightLimitKg(contract.getWeightLimitKg())
                .parcelLimit(contract.getParcelLimit())
                .overWeightFeePerKg(contract.getOverWeightFeePerKg())
                .overParcelFee(contract.getOverParcelFee())
                .specialTerms(contract.getSpecialTerms())
                .createdAt(DateUtils.format(contract.getCreatedAt()))
                .updatedAt(DateUtils.format(contract.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }


    @Override
    public Page<GetAllContractResponseDto> getMyContracts(UserPrincipal userPrincipal, int page, int size, String sort) {
        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));
        Customer customer = customerRepository.findByUser(user).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Page<GetAllContractResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<Contract> contracts = contractRepository.findByCustomerId(customer.getId(), pageable);

        data = contracts.map(this::toGetAllContractResponseDto);

        return data;
    }

    @Override
    @Transactional
    public ResponseDto<Void> deleteContract(UserPrincipal userPrincipal, Long contractId) {
        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));
        Contract contract = contractRepository.findById(contractId).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        if(contract.getStatus() == ContractStatus.DELETED) {
            return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        }

        contract.setStatus(ContractStatus.DELETED);
        contractRepository.save(contract);

        deleteLogService.createLog(TableRef.CONTRACT, contractId, user);

        String alertMessage = "계약 #" + contractId + "이 삭제되었습니다.";
        alertService.sendToUser(contract.getCustomer().getUser().getId(), alertMessage);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    private ContractUpdateLog buildLog(Contract contract, User user, String username, String type, String prevData, String newData) {
        return ContractUpdateLog.builder()
                .contract(contract)
                .user(user)
                .changedByUsername(username)
                .type(type)
                .prevData(String.valueOf(prevData))
                .newData(String.valueOf(newData))
                .build();
    }

    private GetAllContractResponseDto toGetAllContractResponseDto(Contract contract) {
        return GetAllContractResponseDto.builder()
                .id(contract.getId())
                .customerId(contract.getCustomer().getId())
                .customerName(contract.getCustomer().getName())
                .status(contract.getStatus())
                .startDate(contract.getStartDate())
                .endDate(contract.getEndDate())
                .createdAt(DateUtils.format(contract.getCreatedAt()))
                .updatedAt(DateUtils.format(contract.getUpdatedAt()))
                .build();
    }

}

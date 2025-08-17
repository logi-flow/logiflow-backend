package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.enums.DeleteType;
import com.logi_flow.backend.common.enums.TableRef;
import com.logi_flow.backend.entity.DeleteLog;
import com.logi_flow.backend.entity.User;
import com.logi_flow.backend.repository.DeleteLogRepository;
import com.logi_flow.backend.service.DeleteLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DeleteLogServiceImpl implements DeleteLogService {

    private final DeleteLogRepository deleteLogRepository;

    @Override
    @Transactional
    public void createLog(TableRef table, Long recordId, User user) {
        DeleteLog deleteLog = DeleteLog.builder()
                .tableName(table.getValue())
                .recordId(recordId)
                .expiredAt(expireDate())
                .user(user)
                .deleteType(DeleteType.SOFT)
                .build();

        deleteLogRepository.save(deleteLog);
    }

    @Override
    @Transactional
    public void removeIfExists(TableRef table, Long recordId) {
        deleteLogRepository.findByTableNameAndRecordId(table.getValue(), recordId)
                .ifPresent(deleteLogRepository::delete);
    }

    private LocalDate expireDate() {
        return LocalDate.now().plusMonths(3);
    }
}

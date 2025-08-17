package com.logi_flow.backend.service;

import com.logi_flow.backend.common.enums.TableRef;
import com.logi_flow.backend.entity.User;

public interface DeleteLogService {
    void createLog(TableRef table, Long recordId, User user);
    void removeIfExists(TableRef table, Long recordId);
}

package com.logi_flow.backend.dto.stats.driverJoinLeave.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class DriverJoinLeavePoint {
    private String yearMonth;
    private int newHires;
    private int leavers;
}

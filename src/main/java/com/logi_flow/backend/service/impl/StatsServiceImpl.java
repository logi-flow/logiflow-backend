package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.constants.ResponseCode;
import com.logi_flow.backend.common.constants.ResponseMessage;
import com.logi_flow.backend.common.enums.TableRef;
import com.logi_flow.backend.common.enums.driver.DriverStatus;
import com.logi_flow.backend.common.util.DateUtils;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.stats.driverJoinLeave.response.DriverJoinLeavePoint;
import com.logi_flow.backend.dto.stats.driverJoinLeave.response.GetDriverJoinLeaveResponseDto;
import com.logi_flow.backend.repository.DeleteLogRepository;
import com.logi_flow.backend.repository.DriverRepository;
import com.logi_flow.backend.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final DriverRepository driverRepository;
    private final DeleteLogRepository deleteLogRepository;

    @Override
    public ResponseDto<GetDriverJoinLeaveResponseDto> getDriverJoinLeave(YearMonth from, YearMonth to) {
        GetDriverJoinLeaveResponseDto data = null;

        YearMonth now = YearMonth.now();
        YearMonth toDate = (to == null) ? now : to;
        YearMonth fromDate = (from == null) ? toDate.minusMonths(5) : from;

        if (fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException(ResponseMessage.INVALID_DATE_RANGE);
        }

        List<DriverJoinLeavePoint> points = new ArrayList<>();
        YearMonth cursor = fromDate;

        while (!cursor.isAfter(toDate)) {
            LocalDate start = cursor.atDay(1);
            LocalDate end = cursor.plusMonths(1).atDay(1);

            int joins = Math.toIntExact(
                    driverRepository.countByCompanyJoinGreaterThanEqualAndCompanyJoinLessThan(start, end)
            );

            int leaves = Math.toIntExact(
                    deleteLogRepository.countByTableNameAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(TableRef.DRIVER.getValue(), start.atStartOfDay(), end.atStartOfDay())
            );

            points.add(DriverJoinLeavePoint.builder()
                    .yearMonth(DateUtils.yearMonthFormat(cursor))
                    .joins(joins)
                    .leaves(leaves)
                    .build()
            );

            cursor = cursor.plusMonths(1);
        }

        data = GetDriverJoinLeaveResponseDto.builder()
                .from(DateUtils.yearMonthFormat(fromDate))
                .to(DateUtils.yearMonthFormat(toDate))
                .points(points)
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }
}

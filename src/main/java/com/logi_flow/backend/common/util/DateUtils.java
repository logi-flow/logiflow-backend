package com.logi_flow.backend.common.util;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter YEAR_MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    public static String format(LocalDateTime dateTime) {
        return (dateTime != null) ? dateTime.format(FORMATTER) : null;
    }

    public static LocalDateTime parse(String date) {
        return (date != null && !date.isEmpty()) ? LocalDateTime.parse(date, FORMATTER) : null;
    }

    public static String yearDateFormat(LocalDateTime dateTime) {
        return (dateTime != null) ? dateTime.format(YEAR_MONTH_FORMATTER) : null;
    }

    public static LocalDateTime yearDateParse(String dateString) {
        if(dateString == null || dateString.isEmpty()) return null;

        String fixed = dateString + "-01T00:00:00";
        return LocalDateTime.parse(fixed);
    }

    public static String yearMonthFormat(YearMonth date) {
        return (date != null) ? date.format(YEAR_MONTH_FORMATTER) : null;
    }

}
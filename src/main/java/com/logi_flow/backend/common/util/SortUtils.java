package com.logi_flow.backend.common.util;

import org.springframework.data.domain.Sort;

public final class SortUtils {
    private SortUtils() {}

    public static Sort parseCreatedAtSort(String sort) {
        if ("asc".equalsIgnoreCase(sort)) {
            return Sort.by(Sort.Direction.ASC, "createdAt");
        }

        return Sort.by(Sort.Direction.DESC, "createdAt");
    }
}
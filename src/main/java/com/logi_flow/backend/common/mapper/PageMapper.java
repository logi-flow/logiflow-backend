package com.logi_flow.backend.common.mapper;

import com.logi_flow.backend.dto.PageDto;
import org.springframework.data.domain.Page;

import java.util.Map;

public final class PageMapper {
    private PageMapper() {}

    public static <D> PageDto<D> toPageDto(Page<D> page, String sort) {
        return new PageDto<> (
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast(),
                page.hasNext(),
                page.hasPrevious(),
                sort,
                null
        );
    }

    public static <D> PageDto<D> toPageDto(Page<D> page, String sort, Map<String, Object> filters) {
        return new PageDto<> (
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast(),
                page.hasNext(),
                page.hasPrevious(),
                sort,
                filters
        );
    }
}

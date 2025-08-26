package com.logi_flow.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PageDto<T> {
    private List<T> content;

    private int number;
    private int size;
    private long totalElements;
    private int totalPages;

    private boolean first;
    private boolean last;
    private boolean hasNext;
    private boolean hasPrevious;

    private String sort;
}

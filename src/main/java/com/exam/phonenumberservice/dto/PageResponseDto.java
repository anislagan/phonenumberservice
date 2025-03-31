package com.exam.phonenumberservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class PageResponseDto<T> {
    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}

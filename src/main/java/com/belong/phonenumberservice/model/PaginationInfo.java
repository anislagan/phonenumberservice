package com.belong.phonenumberservice.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaginationInfo {
    private int currentPage;
    private int totalPages;
    private long totalItems;
    private int itemsPerPage;
}

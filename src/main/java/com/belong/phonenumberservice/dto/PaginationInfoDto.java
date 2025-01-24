package com.belong.phonenumberservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaginationInfoDto {
    private int currentPage;
    private int totalPages;
    private long totalItems;
    private int itemsPerPage;
}

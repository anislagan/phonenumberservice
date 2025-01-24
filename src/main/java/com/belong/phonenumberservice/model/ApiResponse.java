package com.belong.phonenumberservice.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse<T> {
    private T data;
    private PaginationInfo pagination;
}

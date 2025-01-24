package com.belong.phonenumberservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponseDto<T> {
    private T data;
    private PaginationInfoDto pagination;
}

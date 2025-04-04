package com.exam.phonenumberservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponseDto {
    private String code;
    private String message;
    private Object details;
}

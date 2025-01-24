package com.belong.phonenumberservice.dto;

import lombok.Data;

@Data
public class PageRequestDto {
    private Integer page = 1;
    private Integer size = 20;
    private String status;
}

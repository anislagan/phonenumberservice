package com.exam.phonenumberservice.dto;

import lombok.Data;

@Data
public class PhoneNumberDto {
    private String id;
    private String number;
    private String customerId;
    private String status;
    private String createdAt;
    private String updatedAt;
}

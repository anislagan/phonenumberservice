package com.belong.phonenumberservice.dto;

import com.belong.phonenumberservice.model.PhoneNumberStatus;
import lombok.Data;

import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PhoneNumberDto {
    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Invalid phone number format")
    private String number;
}


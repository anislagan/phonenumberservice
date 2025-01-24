package com.belong.phonenumberservice.dto;

import lombok.Data;
import jakarta.validation.constraints.Pattern;

@Data
public class PhoneNumberDto {
    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Invalid phone number format")
    private String number;
}


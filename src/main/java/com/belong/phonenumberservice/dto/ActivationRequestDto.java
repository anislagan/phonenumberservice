package com.belong.phonenumberservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ActivationRequestDto {
    @NotNull(message = "Activation code cannot be null")
    @Pattern(regexp = "^[0-9]{6}$", message = "Activation code must be 6 digits")
    private String activationCode;
}

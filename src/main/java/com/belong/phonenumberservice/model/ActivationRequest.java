package com.belong.phonenumberservice.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActivationRequest {
    private String activationCode;
}

package com.exam.phonenumberservice.model;

import com.exam.phonenumberservice.dto.PhoneNumberStatusDto;
import lombok.Data;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class PhoneNumber {
    private UUID id;
    private String number;
    private UUID customerId;
    private PhoneNumberStatusDto status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}



package com.exam.phonenumberservice.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class Customer {
    private UUID id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

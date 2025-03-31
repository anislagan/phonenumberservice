package com.exam.phonenumberservice.repository;

import com.exam.phonenumberservice.model.PhoneNumber;
import com.exam.phonenumberservice.dto.PhoneNumberStatusDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class PhoneNumberRepository {
    private static final Map<UUID, PhoneNumber> phoneNumbers = new ConcurrentHashMap<>();

    static {
        // Customer 1 with two phone numbers
        UUID customer1 = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        addPhoneNumber(
                UUID.fromString("7ba7b810-9dad-11d1-80b4-00c04fd430c8"),
                customer1,
                "+61411111111",
                PhoneNumberStatusDto.ACTIVE,
                LocalDateTime.now().minusDays(30),
                LocalDateTime.now().minusDays(30)
        );
        addPhoneNumber(
                UUID.fromString("8ba7b810-9dad-11d1-80b4-00c04fd430c8"),
                customer1,
                "+61422222222",
                PhoneNumberStatusDto.INACTIVE,
                LocalDateTime.now().minusDays(29),
                LocalDateTime.now().minusDays(25)
        );

        // Customer 2 with one phone number
        UUID customer2 = UUID.fromString("6ba7b810-9dad-11d1-80b4-00c04fd430c8");
        addPhoneNumber(
                UUID.fromString("9ba7b810-9dad-11d1-80b4-00c04fd430c8"),
                customer2,
                "+61433333333",
                PhoneNumberStatusDto.ACTIVE,
                LocalDateTime.now().minusDays(20),
                LocalDateTime.now().minusDays(20)
        );
    }

    private static void addPhoneNumber(UUID id, UUID customerId, String number,
                                       PhoneNumberStatusDto status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        phoneNumbers.put(id, PhoneNumber.builder()
                .id(id)
                .customerId(customerId)
                .number(number)
                .status(status)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build());
    }

    public List<PhoneNumber> findAll(PhoneNumberStatusDto status, int page, int limit) {
        return phoneNumbers.values().stream()
                .filter(number -> status == null || number.getStatus() == status)
                .skip((long) (page - 1) * limit)
                .limit(limit)
                .collect(Collectors.toList());
    }

    public long countAll(PhoneNumberStatusDto status) {
        return phoneNumbers.values().stream()
                .filter(number -> status == null || number.getStatus() == status)
                .count();
    }

    public List<PhoneNumber> findByCustomerId(UUID customerId, PhoneNumberStatusDto status) {
        return phoneNumbers.values().stream()
                .filter(number -> number.getCustomerId().equals(customerId))
                .filter(number -> status == null || number.getStatus() == status)
                .collect(Collectors.toList());
    }

    public Optional<PhoneNumber> findById(UUID id) {
        return Optional.ofNullable(phoneNumbers.get(id));
    }

    public PhoneNumber save(PhoneNumber phoneNumber) {
        phoneNumbers.put(phoneNumber.getId(), phoneNumber);
        return phoneNumber;
    }
}
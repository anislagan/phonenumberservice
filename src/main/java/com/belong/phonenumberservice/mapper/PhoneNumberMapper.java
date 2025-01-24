package com.belong.phonenumberservice.mapper;

import com.belong.phonenumberservice.dto.PhoneNumberDto;
import com.belong.phonenumberservice.dto.PhoneNumberResponseDto;
import com.belong.phonenumberservice.model.PhoneNumber;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PhoneNumberMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    public PhoneNumberResponseDto toDto(PhoneNumber phoneNumber) {
        PhoneNumberResponseDto dto = new PhoneNumberResponseDto();
        dto.setId(phoneNumber.getId().toString());
        dto.setNumber(phoneNumber.getNumber());
        dto.setCustomerId(phoneNumber.getCustomerId().toString());
        dto.setStatus(phoneNumber.getStatus().name());
        dto.setCreatedAt(phoneNumber.getCreatedAt().format(DATE_FORMATTER));
        dto.setUpdatedAt(phoneNumber.getUpdatedAt().format(DATE_FORMATTER));
        return dto;
    }

    public List<PhoneNumberResponseDto> toDtoList(List<PhoneNumber> phoneNumbers) {
        return phoneNumbers.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public PhoneNumber toEntity(PhoneNumberDto dto) {
        return PhoneNumber.builder()
                .number(dto.getNumber())
                .build();
    }
}

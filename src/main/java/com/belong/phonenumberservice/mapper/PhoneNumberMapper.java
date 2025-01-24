package com.belong.phonenumberservice.mapper;

import com.belong.phonenumberservice.dto.PhoneNumberDto;
import com.belong.phonenumberservice.model.PhoneNumber;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PhoneNumberMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    public static PhoneNumberDto toDto(PhoneNumber phoneNumber) {
        PhoneNumberDto dto = new PhoneNumberDto();
        dto.setId(phoneNumber.getId().toString());
        dto.setNumber(phoneNumber.getNumber());
        dto.setCustomerId(phoneNumber.getCustomerId().toString());
        dto.setStatus(phoneNumber.getStatus().name());
        dto.setCreatedAt(phoneNumber.getCreatedAt().format(DATE_FORMATTER));
        dto.setUpdatedAt(phoneNumber.getUpdatedAt().format(DATE_FORMATTER));
        return dto;
    }

    public static List<PhoneNumberDto> toDtoList(List<PhoneNumber> phoneNumbers) {
        return phoneNumbers.stream()
                .map(PhoneNumberMapper::toDto)
                .collect(Collectors.toList());
    }
}

package com.belong.phonenumberservice.controller;

import com.belong.phonenumberservice.dto.ApiResponseDto;
import com.belong.phonenumberservice.dto.PhoneNumberDto;
import com.belong.phonenumberservice.dto.PhoneNumberStatusDto;
import com.belong.phonenumberservice.service.PhoneNumberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PhoneNumberController {

    private final PhoneNumberService phoneNumberService;

    @GetMapping("/phone-numbers")
    public ResponseEntity<ApiResponseDto<List<PhoneNumberDto>>> getAllPhoneNumbers(
            @RequestParam(required = false) PhoneNumberStatusDto status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit) {

        ApiResponseDto<List<PhoneNumberDto>> response = phoneNumberService.getAllPhoneNumbers(status, page, limit);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customers/{customerId}/phone-numbers")
    public ResponseEntity<ApiResponseDto<List<PhoneNumberDto>>> getCustomerPhoneNumbers(
            @PathVariable UUID customerId,
            @RequestParam(required = false) PhoneNumberStatusDto status) {

        List<PhoneNumberDto> numbers = phoneNumberService.getCustomerPhoneNumbers(customerId, status);
        ApiResponseDto<List<PhoneNumberDto>> response = ApiResponseDto.<List<PhoneNumberDto>>builder()
                .data(numbers)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/phone-numbers/{phoneNumberId}/activate")
    public ResponseEntity<ApiResponseDto<PhoneNumberDto>> activatePhoneNumber(
            @PathVariable UUID phoneNumberId) {

        PhoneNumberDto activatedNumber = phoneNumberService.activatePhoneNumber(phoneNumberId);
        ApiResponseDto<PhoneNumberDto> response = ApiResponseDto.<PhoneNumberDto>builder()
                .data(activatedNumber)
                .build();
        return ResponseEntity.ok(response);
    }
}
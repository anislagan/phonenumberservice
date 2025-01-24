package com.belong.phonenumberservice.controller;

import com.belong.phonenumberservice.dto.ActivationRequestDto;
import com.belong.phonenumberservice.dto.ApiResponseDto;
import com.belong.phonenumberservice.dto.PhoneNumberResponseDto;
import com.belong.phonenumberservice.dto.PhoneNumberStatusDto;
import com.belong.phonenumberservice.model.*;
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
    public ResponseEntity<ApiResponseDto<List<PhoneNumber>>> getAllPhoneNumbers(
            @RequestParam(required = false) PhoneNumberStatusDto status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit) {

        ApiResponseDto<List<PhoneNumber>> response = phoneNumberService.getAllPhoneNumbers(status, page, limit);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customers/{customerId}/phone-numbers")
    public ResponseEntity<ApiResponseDto<List<PhoneNumber>>> getCustomerPhoneNumbers(
            @PathVariable UUID customerId,
            @RequestParam(required = false) PhoneNumberStatusDto status) {

        List<PhoneNumber> numbers = phoneNumberService.getCustomerPhoneNumbers(customerId, status);
        ApiResponseDto<List<PhoneNumber>> response = ApiResponseDto.<List<PhoneNumber>>builder()
                .data(numbers)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/phone-numbers/{phoneNumberId}/activate")
    public ResponseEntity<ApiResponseDto<PhoneNumberResponseDto>> activatePhoneNumber(
            @PathVariable UUID phoneNumberId,
            @RequestBody @Valid ActivationRequestDto request) {

        PhoneNumberResponseDto activatedNumber = phoneNumberService.activatePhoneNumber(phoneNumberId, request.getActivationCode());
        ApiResponseDto<PhoneNumberResponseDto> response = ApiResponseDto.<PhoneNumberResponseDto>builder()
                .data(activatedNumber)
                .build();
        return ResponseEntity.ok(response);
    }
}
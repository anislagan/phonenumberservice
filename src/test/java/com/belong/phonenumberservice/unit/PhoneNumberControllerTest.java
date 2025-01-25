package com.belong.phonenumberservice.unit;

import com.belong.phonenumberservice.controller.PhoneNumberController;
import com.belong.phonenumberservice.dto.*;
import com.belong.phonenumberservice.mapper.PhoneNumberMapper;
import com.belong.phonenumberservice.model.PhoneNumber;
import com.belong.phonenumberservice.service.PhoneNumberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PhoneNumberController.class)
class PhoneNumberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PhoneNumberService phoneNumberService;

    @MockitoBean
    private PhoneNumberMapper phoneNumberMapper;

    private PhoneNumber testPhoneNumber;
    private UUID testId;
    private UUID testCustomerId;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testCustomerId = UUID.randomUUID();
        testPhoneNumber = PhoneNumber.builder()
                .id(testId)
                .number("+1234567890")
                .customerId(testCustomerId)
                .status(PhoneNumberStatusDto.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @WithMockUser
    void getAllPhoneNumbers_ShouldReturnPhoneNumbers() throws Exception {
        // Arrange
        var response = ApiResponseDto.<List<PhoneNumberDto>>builder()
                .data(PhoneNumberMapper.toDtoList(Arrays.asList(testPhoneNumber)))
                .pagination(PaginationInfoDto.builder()
                        .currentPage(1)
                        .totalPages(1)
                        .totalItems(1)
                        .itemsPerPage(20)
                        .build())
                .build();
        when(phoneNumberService.getAllPhoneNumbers(any(), anyInt(), anyInt()))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/v1/phone-numbers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.pagination").exists());
    }

    @Test
    @WithMockUser
    void getCustomerPhoneNumbers_ShouldReturnCustomerNumbers() throws Exception {
        // Arrange
        when(phoneNumberService.getCustomerPhoneNumbers(testCustomerId, null))
                .thenReturn(PhoneNumberMapper.toDtoList(Arrays.asList(testPhoneNumber)));

        // Act & Assert
        mockMvc.perform(get("/api/v1/customers/{customerId}/phone-numbers", testCustomerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    @WithMockUser
    void activatePhoneNumber_ShouldActivateNumber() throws Exception {
        // Arrange
        when(phoneNumberService.activatePhoneNumber(testId))
                .thenReturn(PhoneNumberMapper.toDto(testPhoneNumber));

        // Act & Assert
        mockMvc.perform(post("/api/v1/phone-numbers/{phoneNumberId}/activate", testId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists());
    }
}
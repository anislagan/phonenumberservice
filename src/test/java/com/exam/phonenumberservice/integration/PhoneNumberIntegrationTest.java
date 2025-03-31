package com.exam.phonenumberservice.integration;

import com.exam.phonenumberservice.model.Customer;
import com.exam.phonenumberservice.model.PhoneNumber;
import com.exam.phonenumberservice.dto.PhoneNumberStatusDto;
import com.exam.phonenumberservice.repository.CustomerRepository;
import com.exam.phonenumberservice.repository.PhoneNumberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PhoneNumberIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PhoneNumberRepository phoneNumberRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID testId;
    private UUID testCustomerId;


    @BeforeEach
    void setUp() {
        // Clean up the repository
        phoneNumberRepository = new PhoneNumberRepository();
        customerRepository = new CustomerRepository();

        testId = UUID.randomUUID();
        testCustomerId = UUID.randomUUID();
        PhoneNumber testPhoneNumber = PhoneNumber.builder()
                .id(testId)
                .number("+1234567890")
                .customerId(testCustomerId)
                .status(PhoneNumberStatusDto.INACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        phoneNumberRepository.save(testPhoneNumber);

        Customer testCustomer = Customer.builder()
                .id(testCustomerId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        customerRepository.save(testCustomer);
    }

    @Test
    @WithMockUser
    void getAllPhoneNumbers_ShouldReturnPhoneNumbers() throws Exception {
        mockMvc.perform(get("/api/v1/phone-numbers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.pagination").exists());
    }

    @Test
    @WithMockUser
    void getCustomerPhoneNumbers_ShouldReturnCustomerNumbers() throws Exception {
        mockMvc.perform(get("/api/v1/customers/{customerId}/phone-numbers", testCustomerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    @WithMockUser
    void getCustomerPhoneNumbers_ShouldReturnNotFound_WhenCustomerDoesNotExist() throws Exception {
        UUID nonExistentCustomerId = UUID.randomUUID();
        mockMvc.perform(get("/api/v1/customers/{customerId}/phone-numbers", nonExistentCustomerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("CUSTOMER_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Customer not found"));
    }

    @Test
    @WithMockUser
    void activatePhoneNumber_ShouldActivateNumber() throws Exception {
        mockMvc.perform(post("/api/v1/phone-numbers/{phoneNumberId}/activate", testId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("ACTIVE"));
    }

    @Test
    @WithMockUser
    void activatePhoneNumber_ShouldReturnNotFound_WhenPhoneNumberDoesNotExist() throws Exception {
        mockMvc.perform(post("/api/v1/phone-numbers/{phoneNumberId}/activate", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void getCustomerPhoneNumbers_ShouldReturnMultiplePhoneNumbersForOneCustomer() throws Exception {
        // Arrange
        UUID customerId = UUID.randomUUID();
        Customer customer = Customer.builder()
                .id(customerId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        customerRepository.save(customer);

        PhoneNumber firstNumber = PhoneNumber.builder()
                .id(UUID.randomUUID())
                .number("+1234567890")
                .customerId(customerId)
                .status(PhoneNumberStatusDto.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        PhoneNumber secondNumber = PhoneNumber.builder()
                .id(UUID.randomUUID())
                .number("+1234567891")
                .customerId(customerId)
                .status(PhoneNumberStatusDto.INACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        phoneNumberRepository.save(firstNumber);
        phoneNumberRepository.save(secondNumber);

        // Act & Assert
        mockMvc.perform(get("/api/v1/customers/{customerId}/phone-numbers", customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[?(@.customerId == '" + customerId + "')]").exists())
                .andExpect(jsonPath("$.data[?(@.number == '+1234567890')]").exists())
                .andExpect(jsonPath("$.data[?(@.number == '+1234567891')]").exists());
    }
}